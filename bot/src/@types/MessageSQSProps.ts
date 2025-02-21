import { ConnectionStatus } from "./ConnectionStatus";

export interface MessageToBeSentSQSProps {
  body: MessageSQS | MessageConnectionStatusProps;
  messageGroupId: string;
  type: "progress" | "status";
}

type MessageSQS = {
  sentChats: number;
  unsentChats: number;
  totalChats: number;
};

type MessageConnectionStatusProps = {
  status: ConnectionStatus;
  sessionId: string;
};
