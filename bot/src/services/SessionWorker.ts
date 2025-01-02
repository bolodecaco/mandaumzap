import { parentPort, workerData } from "worker_threads";
import SessionRepository from "../repository/SessionRepository";
import Session from "../models/Session";
import { ParentMessageProps } from "../@types/ParentMessageProps";
import { SignalsProps } from "../@types/SignalsProps";

let session: Session;

const signalsActions: SignalsProps = {
  initialize: async (message: ParentMessageProps) => {
    const sessionId = message.data.sessionId;
    session = SessionRepository.createSession(sessionId);
    const { qrcode } = await session.connect();
    parentPort?.postMessage({ type: "qrcode", data: qrcode });
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
      await session.getWASocket()?.sendMessage(chat, { text });
    }
  },
  close: async () => {
    if (!session) {
      parentPort?.postMessage({
        type: "error",
        data: "Session not initialized",
      });
      return;
    }
    session.getWASocket()?.end(new Error("Closed by user"));
    process.exit(0);
  },
  delete: async () => {
    if (!session) {
      parentPort?.postMessage({
        type: "error",
        data: "Session not initialized",
      });
      return;
    }
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
