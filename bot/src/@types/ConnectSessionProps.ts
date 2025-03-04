import { WASocket } from "baileys";
import { ConnectionStatus } from "./ConnectionStatus";

export interface ConnectSessionProps {
  socket: WASocket;
  qrcode: string;
  status: ConnectionStatus;
}
