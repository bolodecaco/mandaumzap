export interface ParentMessageProps {
  type: TypeMessageProps;
  data?: any;
}

export type TypeMessageProps =
  | "initialize"
  | "sendText"
  | "sendImage"
  | "sendVideo"
  | "sendPoll"
  | "getChats"
  | "close"
  | "delete";
