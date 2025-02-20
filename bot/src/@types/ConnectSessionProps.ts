import { WASocket } from "@whiskeysockets/baileys";
import { ConnectionStatus } from "./ConnectionStatus";

export interface ConnectSessionProps {
  socket: WASocket;
  qrcode: string;
  status: ConnectionStatus;
  createdAt: Date | null;
}
