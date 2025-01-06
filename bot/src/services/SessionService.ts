import { Worker } from "worker_threads";
import { resolve } from "path";
import { MessageTextProps } from "../@types/MessageTextProps";
import MongoConnection from "../adapters/MongoConnection";
import pino from "pino";
import { UserMongoProps } from "../@types/UserMongoProps";

class SessionService {
  private sessions: Map<string, Worker> = new Map<string, Worker>();
  private mongoConnection: MongoConnection = new MongoConnection({
    logger: pino({ level: "fatal" }),
    sessionId: "main",
  });

  constructor() {
    this.mongoConnection.connectToMongo();
    this.mongoConnection.init();
  }

  private createSession(sessionId: string): Worker {
    const workerPath = resolve(__dirname, "SessionWorker.js");
    const worker = new Worker(workerPath, {
      workerData: { sessionId },
    });

    worker.on("error", (err) => {
      console.error(`Worker error in session ${sessionId}:`, err);
    });

    worker.on("exit", (code) => {
      if (code !== 0) {
        console.error(
          `Worker for session ${sessionId} stopped with exit code ${code}`
        );
      }
    });

    this.sessions.set(sessionId, worker);
    return worker;
  }

  haveSession(sessionId: string): boolean {
    return this.sessions.has(sessionId);
  }

  async getAll(userId: string) {
    return await this.mongoConnection.getSessionByUserId(userId);
  }

  async connectSession({ sessionId, userId }: UserMongoProps): Promise<any> {
    try {
      if (this.haveSession(sessionId)) return { error: "Sessão já existe" };
      const userSession = await this.mongoConnection.getSession({
        sessionId,
        userId,
      });
      if (!userSession.allowed && userSession.exists)
        return { error: "Usuário não autorizado" };
      if (!userSession.exists)
        await this.mongoConnection.addUser({ sessionId, userId });
      const worker = this.createSession(sessionId);
      return new Promise((resolve, reject) => {
        worker.postMessage({
          type: "initialize",
          data: { sessionId },
        });
        worker.on("message", (message) => {
          if (message.type === "error" || message.type === "delete")
            this.sessions.delete(sessionId);
          resolve(message.data);
        });

        worker.on("error", (error) => reject(error));
      });
    } catch (error) {}
  }

  async getChats(sessionId: string): Promise<any | false> {
    const worker = this.sessions.get(sessionId);
    if (!worker) {
      return false;
    }

    return new Promise((resolve, reject) => {
      worker.postMessage({ type: "getChats" });

      worker.once("message", (message) => {
        if (message.type === "chats") {
          resolve(message.data);
        }
      });

      worker.on("error", (error) => reject(error));
    });
  }

  async sendText({
    receivers,
    text,
    sessionId,
  }: MessageTextProps): Promise<boolean> {
    const worker = this.sessions.get(sessionId);
    if (!worker) {
      return false;
    }

    worker.postMessage({
      type: "sendText",
      data: { receivers, text },
    });

    return true;
  }

  async closeSession(sessionId: string): Promise<boolean> {
    const worker = this.sessions.get(sessionId);
    if (!worker) {
      return false;
    }

    worker.postMessage({ type: "close" });
    this.sessions.delete(sessionId);
    return true;
  }

  async deleteSession(sessionId: string): Promise<boolean> {
    const worker = this.sessions.get(sessionId);
    if (!worker) {
      return false;
    }

    worker.postMessage({ type: "delete" });
    this.sessions.delete(sessionId);
    return true;
  }
}

export default SessionService;
