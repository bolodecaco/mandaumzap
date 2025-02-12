import { parentPort, workerData } from "worker_threads";
import SessionRepository from "../repository/SessionRepository";
import Session from "../models/Session";
import { ParentMessageProps } from "../@types/ParentMessageProps";
import { SignalsProps } from "../@types/SignalsProps";
import { fakeTyping } from "../utils/functions";
import MessageProducer from "../producer/MessageProducer";
import { MessageMediaProps, MessagePollProps } from "../@types/MessageSendProps";

let session: Session;
const Producer = new MessageProducer();

type MessageToBeSent = { receivers: string[]; text: string };

const formatMessage = {
  text: (text: string) => ({ text }),
  media: ({ url, text }: MessageMediaProps) => ({
    url,
    text,
  }),
  poll: ({ name, values, selectableCount }: MessagePollProps) => ({
    poll: { name, values, selectableCount },
  }),
};

async function genericSend<T>(
  receivers: string[],
  content: T,
  formatMessage: (content: T) => any
) {
  if (!session)
    return parentPort?.postMessage({
      type: "error",
      data: "Session not initialized",
    });

  const progress = {
    sentChats: 0,
    unsentChats: 0,
    totalChats: receivers.length,
  };

  for (const chat of receivers) {
    try {
      const socket = session.getWASocket();
      await fakeTyping(socket, chat);
      const formattedMessage = formatMessage(content);
      await socket?.sendMessage(chat, formattedMessage);
      progress.sentChats++;
    } catch (error) {
      progress.unsentChats++;
    }
    Producer.sendProgress({
      body: { ...progress },
      messageGroupId: crypto.randomUUID(),
      type: "progress",
    });
  }
}


const signalsActions: SignalsProps = {
  initialize: async ({ sessionId }) => {
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
      const { receivers, text } = message.data;
      await genericSend(receivers, text, formatMessage["text"]);
    },
  sendMedia: async (message: ParentMessageProps) => {
    const { receivers, url, text, header } = message.data;
    await genericSend(receivers, { url, text, header }, formatMessage["media"]);
  },
  sendPoll: async (message: ParentMessageProps) => {
    const { receivers, name, values, selectableCount, header } = message.data;
    await genericSend(
      receivers!,
      { name, values, selectableCount, header },
      formatMessage["poll"]
    );
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
    signalsActions[message.type](message.data);
  } else {
    parentPort?.postMessage({ type: "error", data: "Unknown message type" });
  }
});
