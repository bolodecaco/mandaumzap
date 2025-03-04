import { Worker } from "worker_threads";
import { resolve } from "path";
import {
  MessageMediaProps,
  MessagePollProps,
  MessageRecieveProps,
  MessageTextProps,
} from "../@types/MessageSendProps";
import MongoConnection from "../adapters/MongoConnection";
import pino from "pino";
import { UserMongoProps } from "../@types/UserMongoProps";
import MessageConsumer from "../consumer/MessageConsumer";
import { ConnectionStatus } from "../@types/ConnectionStatus";
import { Logger } from "../logger/Logger";

class SessionService {
  private logger: Logger;
  private sessions: Map<string, Worker> = new Map<string, Worker>();
  private mongoConnection: MongoConnection = new MongoConnection({
    logger: pino({ level: "fatal" }),
    sessionId: "main",
  });
  private messageConsumer: MessageConsumer;

  constructor() {
    this.mongoConnection.init();
    this.mongoConnection.connectToMongo();
    this.messageConsumer = new MessageConsumer(this.processMessage.bind(this));
    this.messageConsumer.startPolling();
    this.logger = new Logger();
  }

  private startSession(
    sessionId: string
  ): Promise<{ qrcode: string; status: ConnectionStatus }> {
    const worker = this.createSession(sessionId);
    return new Promise((resolve, reject) => {
      worker.postMessage({
        type: "start",
        data: { sessionId: sessionId },
      });
      worker.on("message", (message) => {
        if (message.type === "error" || message.type === "delete")
          this.sessions.delete(sessionId);
        resolve(message.data);
      });
      worker.on("error", (error) => reject(error));
    });
  }

  private createSession(sessionId: string): Worker {
    const workerPath = resolve(__dirname, "../workers/SessionWorker.js");
    const worker = new Worker(workerPath, {
      workerData: { sessionId },
    });
    worker.on("error", (err) => {});
    worker.on("exit", (code) => {
      this.sessions.delete(sessionId);
      this.logger.writeLog(`Session ${sessionId} exited with code ${code}`);
    });
    this.sessions.set(sessionId, worker);
    return worker;
  }

  private async processMessage(message: AWS.SQS.Message) {
    if (!message.Body) return;
    const { sessionId, userId, text, receivers, type, url } = JSON.parse(
      message.Body
    );
    if (type && type === "progress") return;
    try {
      if (url)
        return await this.sendImage({
          header: { receivers, sessionId, userId },
          url,
          text,
        });

      await this.sendText({ header: { receivers, sessionId, userId }, text });
    } catch (error: any) {
      this.logger.writeLog(`Error processing message: ${error.message}`);
    }
  }

  haveSession(sessionId: string): boolean {
    return this.sessions.has(sessionId);
  }

  async getAll(userId: string) {
    return await this.mongoConnection.getSessionByUserId(userId);
  }

  async checkSession(userSession: UserMongoProps) {
    return await this.mongoConnection.getSession(userSession);
  }

  async connectSession(userSession: UserMongoProps): Promise<any> {
    try {
      if (this.haveSession(userSession.sessionId))
        return { error: "Sessão já existe" };
      const { allowed, exists } = await this.checkSession(userSession);
      if (!allowed && exists) return { error: "Usuário não autorizado" };
      if (!exists) await this.mongoConnection.addUser(userSession);
      return await this.startSession(userSession.sessionId);
    } catch (error: any) {
      this.logger.writeLog(`Error connecting session: ${error.message}`);
    }
  }

  async closeSession(userSession: UserMongoProps): Promise<boolean> {
    const { allowed, exists } = await this.checkSession(userSession);
    if (!exists || !allowed) return false;
    const worker = this.sessions.get(userSession.sessionId);
    if (!worker) return false;
    worker.postMessage({ type: "close" });
    this.sessions.delete(userSession.sessionId);
    return true;
  }

  async deleteSession(userSession: UserMongoProps): Promise<boolean> {
    const { allowed, exists } = await this.checkSession(userSession);
    if (!exists || !allowed) return false;
    const worker = this.sessions.get(userSession.sessionId);
    if (worker) {
      worker.postMessage({ type: "close" });
      this.sessions.delete(userSession.sessionId);
    }
    await this.mongoConnection.deleteSession(userSession);
    return true;
  }

  async deleteAllSessions(userId: string) {
    const sessions = await this.mongoConnection.getAllSessions(userId);
    sessions.forEach((session) => {
      if (this.sessions.has(session)) {
        const worker = this.sessions.get(session);
        worker?.postMessage({ type: "close" });
      }
    });
    await this.mongoConnection.deleteSessions(sessions);
  }

  private async validateSession(userSession: UserMongoProps) {
    const { allowed, exists } = await this.checkSession(userSession);
    if (!exists || !allowed) {
      return {
        isValid: false,
        error: {
          message: "Usuário não autorizado",
          statusCode: 401,
          details: "As credencias para operar com a sessão estam incorretas",
        },
      };
    }
    return { isValid: true };
  }

  async sendText({
    header: { receivers, sessionId, userId },
    text,
  }: MessageTextProps) {
    const validation = await this.validateSession({ sessionId, userId });
    if (!validation.isValid) return { wasSent: false, error: validation.error };

    let worker = this.sessions.get(sessionId);
    if (!worker) await this.startSession(sessionId);
    worker = this.sessions.get(sessionId);
    worker!.postMessage({
      type: "sendText",
      data: { header: { receivers }, text },
    });
    return { wasSent: true };
  }

  async sendImage({
    header: { receivers, sessionId, userId },
    url,
    text,
  }: MessageMediaProps) {
    const validation = await this.validateSession({ sessionId, userId });
    if (!validation.isValid) return { wasSent: false, error: validation.error };

    let worker = this.sessions.get(sessionId);
    if (!worker) await this.startSession(sessionId);
    worker = this.sessions.get(sessionId);
    worker!.postMessage({
      type: "sendImage",
      data: { header: { receivers }, url, text },
    });
    return { wasSent: true };
  }

  async sendPoll({
    header: { receivers, sessionId, userId },
    name,
    values,
    selectableCount,
  }: MessagePollProps) {
    const validation = await this.validateSession({ sessionId, userId });
    if (!validation.isValid) return { wasSent: false, error: validation.error };

    let worker = this.sessions.get(sessionId);
    if (!worker) await this.startSession(sessionId);
    worker = this.sessions.get(sessionId);
    worker!.postMessage({
      type: "sendPoll",
      data: { header: { receivers }, name, values, selectableCount },
    });
    return { wasSent: true };
  }

  async sendVideo({
    header: { receivers, sessionId, userId },
    url,
    text,
  }: MessageMediaProps) {
    const validation = await this.validateSession({ sessionId, userId });
    if (!validation.isValid) return { wasSent: false, error: validation.error };

    let worker = this.sessions.get(sessionId);
    if (!worker) await this.startSession(sessionId);
    worker = this.sessions.get(sessionId);
    worker!.postMessage({
      type: "sendVideo",
      data: { header: { receivers }, url, text },
    });
    return { wasSent: true };
  }

  async getChats(userSession: UserMongoProps) {
    const validation = await this.validateSession(userSession);
    if (!validation.isValid) return { error: validation.error };

    const chats = await this.mongoConnection.getChats(userSession.sessionId);
    return { count: chats.length, chats };
  }
}

export default SessionService;
