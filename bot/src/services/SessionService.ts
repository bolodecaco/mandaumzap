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

  async connectSession(session: Session): Promise<string> {
    const { qrcode, socket } = await session.connect();
    this.sessions.set(session.getId(), socket);
    return qrcode;
  }

  async sendText({ to, text, sessionId }: MessageTextProps) {
    const socketClient = this.sessions.get(sessionId);
    if (!socketClient) {
      throw new Error("Session not found");
    }
    try {
      for (const chat of to) {
        await delay(getBetweenValue({ textLength: text.length }));
        await socketClient.sendMessage(chat, { text });
      }
    } catch (error) {}
  }
}

export default SessionService;
