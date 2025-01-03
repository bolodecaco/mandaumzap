import { WASocket, DisconnectReason, delay } from "@whiskeysockets/baileys";
import { Boom } from "@hapi/boom";
import WASocketWrapper from "./Socket";
import qrcode from "qrcode-terminal";
import { ConnectSessionProps } from "../@types/ConnectSessionProps";

class Session {
  private id: string;
  private waSocket: WASocketWrapper;
  private socketClient: WASocket | null = null;
  private isConnected: boolean;

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

  async getHashToken() {
    return await this.waSocket.getHashToken();
  }

  async getFirstToken() {
    return await this.waSocket.getFirstToken();
  }

  async getChats() {
    return await this.waSocket.getChats();
  }

  async delete() {
    await this.waSocket.removeSession();
    process.exit(0);
  }

  async connect(): Promise<ConnectSessionProps> {
    await this.waSocket.initialize();
    this.socketClient = this.waSocket.getSocket();
    return new Promise((resolve, reject) => {
      this.socketClient!.ev.on("connection.update", async (update) => {
        const statusCode = (update.lastDisconnect?.error as Boom)?.output
          ?.statusCode;
        const { connection, qr } = update;
        if (connection === "open") {
          this.isConnected = true;
          return resolve({ qrcode: "", socket: this.socketClient! });
        }
        if (connection === "close") {
          if (DisconnectReason.restartRequired == statusCode) {
            return this.connect();
          }
          this.delete();
        }
        if (qr && this.id) {
          qrcode.generate(qr, { small: true });
          setTimeout(() => {
            if (!this.isConnected) this.delete();
          }, 50 * 1000); //50 seconds
          resolve({ socket: this.socketClient!, qrcode: qr });
        }
      });
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
