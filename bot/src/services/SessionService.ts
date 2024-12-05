import { WASocket, delay } from "@whiskeysockets/baileys";
import Session from "../models/Session";
import SessionRepository from "../repository/SessionRepository";
import { getBetweenValue } from "../utils/functions";
import { MessageTextProps } from "../@types/MessageTextProps";

class SessionService {
  private sessions: Map<string, WASocket> = new Map<string, WASocket>();

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
    const { qrcode, socket } = await session.connect();
    this.sessions.set(session.getId(), socket);
    return qrcode;
  }

  async sendText({ receivers, text, sessionId }: MessageTextProps) {
    const socketClient = this.sessions.get(sessionId);
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
}

export default SessionService;
