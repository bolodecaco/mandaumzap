import { SQS } from "aws-sdk";
import crypto from "crypto";
import { MessageToBeSentSQSProps } from "../@types/MessageSQSProps";
import SQSClient from "../adapters/SqsClient";

class MessageProducer {
  private sqsClient: SQSClient;

  constructor() {
    this.sqsClient = new SQSClient();
  }

  async sendProgress(message: MessageToBeSentSQSProps) {
    try {
      const params: SQS.Types.SendMessageRequest = {
        QueueUrl: this.sqsClient.url,
        MessageGroupId: message.messageGroupId,
        MessageDeduplicationId: crypto.randomUUID(),
        MessageBody: JSON.stringify(message),
      };
      await this.sqsClient.sqs.sendMessage(params).promise();
    } catch (error) {}
  }
}

export default MessageProducer;
