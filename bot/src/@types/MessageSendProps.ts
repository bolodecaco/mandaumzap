export interface MessageHeaderProps {
  receivers: string[];
  sessionId: string;
  userId: string;
  messageId: string;
}

export interface MessageRecieveProps {
  header: MessageHeaderProps;
  text: string;
  type?: string;
  url?: string;
  values?: string[];
  name?: string;
  selectableCount?: 1 | undefined;
}

export interface MessageTextProps {
  header: MessageHeaderProps;
  text: string;
}

export interface MessageMediaProps {
  header: MessageHeaderProps;
  url: string;
  text?: string;
}

export interface MessagePollProps {
  header: MessageHeaderProps;
  name: string;
  values: string[];
  selectableCount: 1 | undefined;
}
