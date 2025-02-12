export interface ParentMessageProps {
  type: TypeMessageProps;
  data?: any;
}

export type TypeMessageProps =
  | "initialize"
  | "sendText"
  | "sendMedia"
  | "sendPoll"
  | "getChats"
  | "close"
  | "delete";
