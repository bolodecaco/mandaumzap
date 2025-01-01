import { WASocket, delay } from "@whiskeysockets/baileys";
import Session from "../models/Session";
import SessionRepository from "../repository/SessionRepository";
import { getBetweenValue } from "../utils/functions";
import { MessageTextProps } from "../@types/MessageTextProps";

class SessionService {
  private sessions: Map<string, Session> = new Map<string, Session>();

  createSession(sessionId: string): Session {
    return SessionRepository.createSession(sessionId);
  }

  haveSession(sessionId: string): boolean {
    return this.sessions.get(sessionId) ? true : false;
  }

  getAll() {
    return Array.from(this.sessions.keys());
  }

  async connectSession(sessionId: string): Promise<string> {
    const session = this.createSession(sessionId);
    const { qrcode } = await session.connect();
    this.sessions.set(session.getId(), session);
    return qrcode;
  }

  async getChats(sessionId: string) {
    const session = this.createSession(sessionId);
    return await session.getChats();
  }

  async sendText({ receivers, text, sessionId }: MessageTextProps) {
    const socketClient = this.sessions.get(sessionId)?.getWASocket();
    if (!socketClient) {
      return false;
    }
    try {
      for (const chat of receivers) {
        await delay(getBetweenValue({ textLength: text.length }));
        await socketClient.sendMessage(chat, { text });
      }
    } catch (error) {
      return false;
    }
    return true;
  }

  async closeSession(sessionId: string) {
    try {
      const socketClient = this.sessions.get(sessionId)?.getWASocket();
      if (!socketClient) {
        return false;
      }
      socketClient.end(new Error("Closed by user"));
      this.sessions.delete(sessionId);
      return true;
    } catch (error) {
      return false;
    }
  }

  async deleteSession(sessionId: string) {
    try {
      const session = this.sessions.get(sessionId);
      if (!session) {
        return false;
      }
      session.getWASocket().end(new Error("Closed by user"));
      await session.delete();
      this.sessions.delete(sessionId);
      return true;
    } catch (error) {
      return false;
    }
  }
}

export default SessionService;
