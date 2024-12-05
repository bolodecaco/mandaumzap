import { WASocket, DisconnectReason, delay } from "@whiskeysockets/baileys";
import WASocketWrapper from "./Socket";
import qrcode from "qrcode-terminal";
import { ConnectSessionProps } from "../@types/ConnectSessionProps";

class Session {
  private id: string;
  private waSocket: WASocketWrapper;
  private socketClient: WASocket | null = null;

  constructor(id: string) {
    this.id = id;
    this.waSocket = new WASocketWrapper(id);
  }

  getId(): string {
    return this.id;
  }

  async connect(): Promise<ConnectSessionProps> {
    await this.waSocket.initialize();
    this.socketClient = this.waSocket.getSocket();
    return new Promise((resolve, reject) => {
      this.socketClient!.ev.on("connection.update", async (update) => {
        const { connection, qr } = update;
        if (connection === "open") {
          console.log("Session connected");
          return resolve({ qrcode: "", socket: this.socketClient! });
        }
        if (connection === "close") {
          if (DisconnectReason.restartRequired) {
            return this.connect();
          }
        }
        if (qr && this.id) {
          qrcode.generate(qr, { small: true });
          setTimeout(() => {
            if (!this.socketClient!.user) {
              reject("Session not connected");
            }
          }, 50 * 1000);
          resolve({ socket: this.socketClient!, qrcode: qr });
        }
      });
    });
  }
}

export default Session;
