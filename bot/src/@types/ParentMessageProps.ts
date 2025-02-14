export interface ParentMessageProps {
  type: TypeMessageProps;
  data?: any;
}

export type TypeMessageProps =
  | "start"
  | "sendText"
  | "sendImage"
  | "sendVideo"
  | "sendPoll"
  | "getChats"
  | "close"
  | "delete";
