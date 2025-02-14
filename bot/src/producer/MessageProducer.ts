import { SQS } from "aws-sdk";
import crypto from "crypto";
import { MessageToBeSentSQSProps } from "../@types/MessageSQSProps";
import SQSClient from "../adapters/SqsClient";
import { Logger } from "../logger/Logger";

class MessageProducer {
  private sqsClient: SQSClient;
  private logger = new Logger();

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
    } catch (error: any) {
      this.logger.writeLog(`Error sending SQS message: ${error?.message}`);
    }
  }
}

export default MessageProducer;
