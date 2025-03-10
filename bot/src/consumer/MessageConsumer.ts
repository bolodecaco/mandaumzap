import { Message, ReceiveMessageCommandInput } from "@aws-sdk/client-sqs";
import SQSClient from "../adapters/SqsClient";
import { Logger } from "../logger/Logger";
import dotenv from "dotenv";

dotenv.config();

class MessageConsumer {
  private sqsClient: SQSClient;
  private params: ReceiveMessageCommandInput;
  private processMessageCallback: (message: Message) => void;
  private logger = new Logger();

  constructor(processMessageCallback: (message: Message) => void) {
    this.sqsClient = new SQSClient(process.env.SQS_URL!);
    this.processMessageCallback = processMessageCallback;
    this.params = {
      AttributeNames: ["CreatedTimestamp"],
      MaxNumberOfMessages: 10,
      MessageAttributeNames: ["All"],
      QueueUrl: this.sqsClient.url,
      VisibilityTimeout: 20,
      WaitTimeSeconds: 10,
    };
  }

  async receiveMessages() {
    try {
      this.sqsClient.sqs.receiveMessage(this.params, (err, data) => {
        if (err) return;
        if (data?.Messages) {
          data.Messages.forEach((message) => {
            this.processMessageCallback(message);
          });
          this.deleteMessages(data.Messages);
        }
      });
    } catch (error: any) {
      this.logger.writeLog(`Error receiving SQS messages: ${error.message}`);
    }
  }

  private async deleteMessages(messages: Message[]) {
    for (const message of messages) {
      if (message.ReceiptHandle) {
        await this.sqsClient.sqs
          .deleteMessage({
            QueueUrl: this.sqsClient.url,
            ReceiptHandle: message.ReceiptHandle,
          })
      }
    }
  }

  startPolling(interval: number = 5000) {
    setInterval(() => this.receiveMessages(), interval);
  }
}

export default MessageConsumer;
