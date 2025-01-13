import { WASocket } from "@whiskeysockets/baileys";

export interface ConnectSessionProps {
  socket: WASocket;
  qrcode: string;
}
