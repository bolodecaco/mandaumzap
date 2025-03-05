import { SendMessageCommandInput } from "@aws-sdk/client-sqs";
import crypto from "crypto";
import { MessageToBeSentSQSProps } from "../@types/MessageSQSProps";
import SQSClient from "../adapters/SqsClient";
import { Logger } from "../logger/Logger";

class MessageProducer {
  private sqsClient: SQSClient;
  private logger = new Logger();

  constructor(url: string) {
    this.sqsClient = new SQSClient(url);
  }

  async sendMessage(message: MessageToBeSentSQSProps) {
    try {
      const params: SendMessageCommandInput = {
        QueueUrl: this.sqsClient.url,
        MessageGroupId: crypto.randomUUID(),
        MessageDeduplicationId: crypto.randomUUID(),
        MessageBody: JSON.stringify(message.body),
      };
      await this.sqsClient.sqs.sendMessage(params);
    } catch (error: any) {
      this.logger.writeLog(`Error sending SQS message: ${error?.message}`);
    }
  }
}

export default MessageProducer;
