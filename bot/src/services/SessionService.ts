import { Worker } from "worker_threads";
import { resolve } from "path";
import { MessageMediaProps, MessagePollProps, MessageRecieveProps, MessageTextProps } from "../@types/MessageSendProps";
import MongoConnection from "../adapters/MongoConnection";
import pino from "pino";
import { UserMongoProps } from "../@types/UserMongoProps";
import MessageConsumer from "../consumer/MessageConsumer";

class SessionService {
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
  }

  private createSession(sessionId: string): Worker {
    const workerPath = resolve(__dirname, "SessionWorker.js");
    const worker = new Worker(workerPath, {
      workerData: { sessionId },
    });

    worker.on("error", (err) => {});

    worker.on("exit", (code) => {
      console.error(
        `Worker for session ${sessionId} stopped with exit code ${code}`
      );
    });

    this.sessions.set(sessionId, worker);
    return worker;
  }

  private async processMessage(message: AWS.SQS.Message) {
    if (!message.Body) return;
    const parsedMessage: MessageRecieveProps = JSON.parse(message.Body);
    if (parsedMessage.type === "progress") return;
    try {
      await this.sendText(parsedMessage);
    } catch (error) {}
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
      const worker = this.createSession(userSession.sessionId);
      return new Promise((resolve, reject) => {
        worker.postMessage({
          type: "initialize",
          data: { sessionId: userSession.sessionId },
        });
        worker.on("message", (message) => {
          if (message.type === "error" || message.type === "delete")
            this.sessions.delete(userSession.sessionId);
          resolve(message.data);
        });
        worker.on("error", (error) => reject(error));
      });
    } catch (error) {}
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
    if (!worker) return false;
    worker.postMessage({ type: "delete" });
    this.sessions.delete(userSession.sessionId);
    return true;
  }

  async sendText({
    header: { receivers, sessionId, userId },
    text,
  }: MessageTextProps): Promise<boolean> {
    const { allowed, exists } = await this.checkSession({ sessionId, userId });
    if (!exists || !allowed) return false;
    const worker = this.sessions.get(sessionId);
    if (!worker) return false;
    worker.postMessage({
      type: "sendText",
      data: { receivers, text },
    });
    return true;
  }

  async sendImage({
    header: { receivers, sessionId, userId },
    url,
    text,
  }: MessageMediaProps): Promise<boolean> {
    const { allowed, exists } = await this.checkSession({ sessionId, userId });
    if (!exists || !allowed) return false;
    const worker = this.sessions.get(sessionId);
    if (!worker) return false;
    worker.postMessage({
      type: "sendImage",
      data: { receivers, url, text },
    });
    return true;
  }

  async sendPoll({
    header: { receivers, sessionId, userId },
    name,
    values,
    selectableCount,
  }: MessagePollProps): Promise<boolean> {
    const { allowed, exists } = await this.checkSession({ sessionId, userId });
    if (!exists || !allowed) return false;
    const worker = this.sessions.get(sessionId);
    if (!worker) return false;
    worker.postMessage({
      type: "sendPoll",
      data: { receivers, name, values, selectableCount },
    });
    return true;
  }

  async sendVideo({
    header: { receivers, sessionId, userId },
    url,
    text,
  }: MessageMediaProps): Promise<boolean> {
    const { allowed, exists } = await this.checkSession({ sessionId, userId });
    if (!exists || !allowed) return false;
    const worker = this.sessions.get(sessionId);
    if (!worker) return false;
    worker.postMessage({
      type: "sendVideo",
      data: { receivers, url, text },
    });
    return true;
  }


  async getChats(userSession: UserMongoProps) {
    const { allowed, exists } = await this.checkSession(userSession);
    if (!exists || !allowed) return false;
    const chats = await this.mongoConnection.getChats(userSession.sessionId);
    return { count: chats.length, chats };
  }
}

export default SessionService;
