import { ConnectionStatus } from "./ConnectionStatus";

export interface MessageToBeSentSQSProps {
  body: MessageProgressSQS | MessageConnectionStatusSQS;
  messageGroupId: string;
}

type MessageProgressSQS = {
  sentChats: number;
  unsentChats: number;
  totalChats: number;
  type: "progress" | "connection-status";
};

type MessageConnectionStatusSQS = {
  status: ConnectionStatus;
  sessionId: string;
  type: "progress" | "connection-status";
};
