export interface MessageToBeSentSQSProps {
  body: MessageSQS;
  messageGroupId: string;
  type: "progress";
}

type MessageSQS = {
  sentChats: number;
  totalChats: number;
};
