import { WASocket, DisconnectReason, delay } from "@whiskeysockets/baileys";
import { Boom } from "@hapi/boom";
import WASocketWrapper from "./Socket";
import qrcode from "qrcode-terminal";
import { ConnectSessionProps } from "../@types/ConnectSessionProps";
import { parentPort } from "worker_threads";
import MessageProducer from "../producer/MessageProducer";
import dotenv from "dotenv";
import { ConnectionStatus } from "../@types/ConnectionStatus";

dotenv.config();

class Session {
  private id: string;
  private waSocket: WASocketWrapper;
  private socketClient: WASocket | null = null;
  private isConnected: boolean;
  private producerStatus = new MessageProducer(process.env.SQS_STATUS_URL!);

  constructor(id: string) {
    this.id = id;
    this.waSocket = new WASocketWrapper(id);
    this.isConnected = false;
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

  async sendConnectionStatus(status: ConnectionStatus) {
    return await this.producerStatus.sendMessage({
      body: { status, sessionId: this.id },
      type: "status",
    });
  }

  async connect(): Promise<ConnectSessionProps> {
    await this.waSocket.start();
    this.socketClient = this.waSocket.getSocket();
    return new Promise((resolve, reject) => {
      this.socketClient!.ev.on("connection.update", async (update) => {
        const statusCode = (update.lastDisconnect?.error as Boom)?.output
          ?.statusCode;
        const { connection, qr } = update;
        if (connection === "open") {
          await this.sendConnectionStatus("open");
          this.isConnected = true;
          return resolve({
            qrcode: "",
            socket: this.socketClient!,
            status: "open",
          });
        }
        if (connection === "close") {
          if (DisconnectReason.restartRequired == statusCode)
            return this.connect();
          await this.sendConnectionStatus("close");
          resolve({
            socket: this.socketClient!,
            qrcode: "",
            status: "close",
          });
          this.delete();
        }
        if (qr && this.id) {
          setTimeout(() => {
            if (!this.isConnected) this.delete();
          }, 50 * 1000); //50 seconds
          await this.sendConnectionStatus("pending");
          resolve({
            socket: this.socketClient!,
            qrcode: qr,
            status: "pending",
          });
        }
      });
      this.socketClient!.ev.on(
        "messaging-history.set",
        async ({ contacts }) => {
          const chats = contacts.map((contact) => {
            return {
              id: contact.id,
              name: contact.name || contact.notify || "Desconhecido",
            };
          });
          await this.waSocket.addChats(chats);
          await this.sendConnectionStatus("open");
        }
      );
    });
  }
}

export default Session;
