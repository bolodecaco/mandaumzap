import { ConnectionStatus } from "./ConnectionStatus";

export interface MessageToBeSentSQSProps {
  body: MessageSQS | MessageConnectionStatusProps;
  type: "progress" | "status";
}

type MessageSQS = {
  sentChats: number;
  unsentChats: number;
  totalChats: number;
  messageId?: string;
};

type MessageConnectionStatusProps = {
  status: ConnectionStatus;
  sessionId: string;
};
