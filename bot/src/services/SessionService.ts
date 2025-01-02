import { Worker } from "worker_threads";
import { resolve } from "path";
import { MessageTextProps } from "../@types/MessageTextProps";

class SessionService {
  private sessions: Map<string, Worker> = new Map<string, Worker>();

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

  getAll(): string[] {
    return Array.from(this.sessions.keys());
  }

  async connectSession(sessionId: string): Promise<string | false> {
    if (this.haveSession(sessionId)) {
      return false;
    }

    const worker = this.createSession(sessionId);

    return new Promise((resolve, reject) => {
      worker.postMessage({ type: "initialize", data: { sessionId } });

      worker.on("message", (message) => {
        if (message.type === "qrcode") {
          resolve(message.data);
        }
      });

      worker.on("error", (error) => reject(error));
    });
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
