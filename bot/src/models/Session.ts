import {
  WASocket,
  DisconnectReason,
  delay,
  WAConnectionState,
} from "@whiskeysockets/baileys";
import { Boom } from "@hapi/boom";
import WASocketWrapper from "./Socket";
import qrcode from "qrcode-terminal";
import { ConnectSessionProps } from "../@types/ConnectSessionProps";
import { parentPort } from "worker_threads";
import MessageProducer from "../producer/MessageProducer";
import { ConnectionStatus } from "../@types/ConnectionStatus";

class Session {
  private id: string;
  private waSocket: WASocketWrapper;
  private socketClient: WASocket | null = null;
  private connectionStatus: ConnectionStatus;
  private producer: MessageProducer;

  constructor(id: string) {
    this.id = id;
    this.waSocket = new WASocketWrapper(id);
    this.connectionStatus = "pending";
    this.producer = new MessageProducer();
  }

  getId(): string {
    return this.id;
  }

  getWASocket(): WASocket {
    return this.waSocket.getSocket();
  }

  async getChats() {
    return await this.waSocket.getChats();
  }

  async delete() {
    parentPort?.postMessage({
      type: "delete",
      data: { sessionId: this.id, message: "Delete session" },
    });
    await this.waSocket.removeSession();
    process.exit(0);
  }

  private async sendConnectionStatus(status: WAConnectionState) {
    return await this.producer.sendMessage({
      body: { status: "open", sessionId: this.id, type: "connection-status" },
      messageGroupId: "connection-status",
    });
  }

  async connect(): Promise<ConnectSessionProps> {
    await this.waSocket.start();
    this.socketClient = this.waSocket.getSocket();
    const createdAt = await this.waSocket.getCreatedAt();
    return new Promise((resolve, reject) => {
      this.socketClient!.ev.on(
        "connection.update",
        async ({ connection, qr, lastDisconnect }) => {
          const statusCode = (lastDisconnect?.error as Boom)?.output
            ?.statusCode;
          await this.sendConnectionStatus(connection!);
          if (connection === "open") {
            this.connectionStatus = "open";
            return resolve({
              qrcode: "",
              socket: this.socketClient!,
              status: "open",
              createdAt,
            });
          }
          if (connection === "close") {
            if (DisconnectReason.restartRequired == statusCode)
              return this.connect();
            resolve({
              socket: this.socketClient!,
              qrcode: "",
              status: "close",
              createdAt,
            });
            this.delete();
          }
          if (qr && this.id) {
            qrcode.generate(qr, { small: true });
            setTimeout(() => {
              if (this.connectionStatus != "open") this.delete();
            }, 50 * 1000);
            resolve({
              socket: this.socketClient!,
              qrcode: qr,
              status: "pending",
              createdAt,
            });
          }
        }
      );
      this.socketClient!.ev.on("messaging-history.set", ({ contacts }) => {
        const chats = contacts.map((contact) => {
          return { id: contact.id, name: contact.name || "Desconhecido" };
        });
        this.waSocket.addChats(chats);
      });
      this.socketClient!.ev.on("contacts.upsert", (contacts) => {
        const chats = contacts.map((contact) => {
          return {
            id: contact.id,
            name: contact.notify || contact.verifiedName || "Desconhecido",
          };
        });
        this.waSocket.addChats(chats);
      });
    });
  }
}

export default Session;
