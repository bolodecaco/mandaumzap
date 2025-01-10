import { parentPort, workerData } from "worker_threads";
import SessionRepository from "../repository/SessionRepository";
import Session from "../models/Session";
import { ParentMessageProps } from "../@types/ParentMessageProps";
import { SignalsProps } from "../@types/SignalsProps";
import { error } from "console";
import { fakeTyping } from "../utils/functions";

let session: Session;

const signalsActions: SignalsProps = {
  initialize: async (message: ParentMessageProps) => {
    const { sessionId } = message.data;
    session = SessionRepository.createSession(sessionId);
    const { qrcode } = await session.connect();
    parentPort?.postMessage({
      type: "qrcode",
      data: { qrcode },
    });
  },
  getChats: async () => {
    if (!session) {
      parentPort?.postMessage({
        type: "error",
        data: "Session not initialized",
      });
      return;
    }
    const chats = await session.getChats();
    parentPort?.postMessage({ type: "chats", data: chats });
  },
  sendText: async (message: ParentMessageProps) => {
    if (!session) {
      parentPort?.postMessage({
        type: "error",
        data: "Session not initialized",
      });
      return;
    }
    const { receivers, text } = message.data;
    for (const chat of receivers) {
      const socket = session.getWASocket();
      await fakeTyping(socket, chat);
      await session.getWASocket()?.sendMessage(chat, { text });
    }
  },
  close: async () => {
    if (!session) return;

    parentPort?.postMessage({
      type: "close",
      data: "Close session",
    });
    session.getWASocket()?.end(new Error("Closed by user"));
    process.exit(0);
  },
  delete: async () => {
    if (!session) return;
    parentPort?.postMessage({
      type: "delete",
      data: "Delete session",
    });
    session.getWASocket()?.end(new Error("Closed by user"));
    await session.delete();
    process.exit(0);
  },
};

parentPort?.on("message", async (message: ParentMessageProps) => {
  if (signalsActions[message.type]) {
    signalsActions[message.type](message);
  } else {
    parentPort?.postMessage({ type: "error", data: "Unknown message type" });
  }
});
