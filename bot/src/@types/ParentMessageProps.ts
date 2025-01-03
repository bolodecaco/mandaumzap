export interface ParentMessageProps {
  type: TypeMessageProps;
  data?: any;
}

export type TypeMessageProps =
  | "initialize"
  | "sendText"
  | "getChats"
  | "close"
  | "delete";
