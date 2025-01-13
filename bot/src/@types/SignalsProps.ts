import { ParentMessageProps } from "./ParentMessageProps";

export interface SignalsProps {
  initialize: (message: ParentMessageProps) => void;
  getChats: () => void;
  sendText: (message: ParentMessageProps) => void;
  close: () => void;
  delete: () => void;
}
