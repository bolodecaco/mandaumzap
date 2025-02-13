import { MessageMediaProps, MessagePollProps, MessageTextProps } from "./MessageSendProps";
import { ParentMessageProps } from "./ParentMessageProps";
type InitSessionProps = { sessionId: string };

export interface SignalsProps {
  initialize: (content: InitSessionProps) => void;
  getChats: () => void;
  sendText: (message: MessageTextProps) => void;
  sendImage: (message: MessageMediaProps) => void;
  sendVideo: (message: MessageMediaProps) => void;
  sendPoll: (message: MessagePollProps) => void;
  close: () => void;
  delete: () => void;
}

