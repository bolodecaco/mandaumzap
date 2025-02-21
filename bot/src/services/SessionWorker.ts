import { parentPort } from "worker_threads";
import SessionRepository from "../repository/SessionRepository";
import Session from "../models/Session";
import { ParentMessageProps } from "../@types/ParentMessageProps";
import { SignalsProps } from "../@types/SignalsProps";
import { fakeTyping } from "../utils/functions";
import MessageProducer from "../producer/MessageProducer";
import {
  MessageMediaProps,
  MessagePollProps,
  MessageTextProps,
} from "../@types/MessageSendProps";
import dotenv from "dotenv";

dotenv.config();

let session: Session;
const Producer = new MessageProducer(process.env.SQS_URL!);

const formatMessage = {
  text: (text: string) => ({ text }),
  image: ({ url, text }: MessageMediaProps) => ({
    image: { url },
    caption: text,
  }),
  video: ({ url, text }: MessageMediaProps) => ({
    video: { url },
    caption: text,
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
    Producer.sendMessage({
      body: { ...progress },
      messageGroupId: crypto.randomUUID(),
      type: "progress",
    });
  }
}

const signalsActions: SignalsProps = {
  start: async ({ sessionId }) => {
    session = SessionRepository.createSession(sessionId);
    const { qrcode, status } = await session.connect();
    parentPort?.postMessage({
      type: "qrcode",
      data: { qrcode, status },
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
  sendText: async ({ header, text }: MessageTextProps) => {
    await genericSend(header.receivers, text, formatMessage["text"]);
  },
  sendImage: async ({ header, url, text }: MessageMediaProps) => {
    await genericSend(
      header.receivers,
      { url, text, header },
      formatMessage["image"]
    );
  },
  sendVideo: async ({ header, url, text }: MessageMediaProps) => {
    await genericSend(
      header.receivers,
      { url, text, header },
      formatMessage["video"]
    );
  },
  sendPoll: async ({
    header,
    name,
    selectableCount,
    values,
  }: MessagePollProps) => {
    await genericSend(
      header.receivers!,
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
  try {
    if (signalsActions[message.type]) {
      signalsActions[message.type](message.data);
    } else {
      parentPort?.postMessage({ type: "error", data: "Unknown message type" });
    }
  } catch (error: any) {
    parentPort?.postMessage({ type: "error", data: error.message });
  }
});
