import { ParentMessageProps } from "./ParentMessageProps";
type InitSessionProps = { sessionId: string };

export interface SignalsProps {
  initialize: (content: InitSessionProps) => void;
  getChats: () => void;
  sendText: (message: ParentMessageProps) => void;
  sendMedia: (message: ParentMessageProps) => void;
  sendPoll: (message: ParentMessageProps) => void;
  close: () => void;
  delete: () => void;
}

