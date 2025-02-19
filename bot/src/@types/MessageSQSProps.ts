import { WAConnectionState } from "@whiskeysockets/baileys";

export interface MessageToBeSentSQSProps {
  body: MessageProgressSQS | MessageConnectionStatusSQS;
  messageGroupId: string;
  type: "progress" | "connection-status";
}

type MessageProgressSQS = {
  sentChats: number;
  unsentChats: number;
  totalChats: number;
};

type MessageConnectionStatusSQS = {
  status: WAConnectionState;
};
