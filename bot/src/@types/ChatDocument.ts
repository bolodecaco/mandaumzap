import { WithId, Document } from "mongodb";
import { ChatProps } from "./ChatProps";

export interface ChatsDocument extends WithId<Document> {
  sessionId: string;
  chats: ChatProps[];
}
