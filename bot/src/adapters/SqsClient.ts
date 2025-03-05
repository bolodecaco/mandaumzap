import { SQS } from "@aws-sdk/client-sqs";
import dotenv from "dotenv";

dotenv.config();
const region = process.env.SQS_REGION;
// JS SDK v3 does not support global configuration.
// Codemod has attempted to pass values to each service client in this file.
// You may need to update clients outside of this file, if they use global config.

class SQSClient {
  public readonly sqs: SQS;
  public readonly url: string;
  private accessKey = process.env.SQS_ACCESS_KEY_ID;
  private secretKey = process.env.SQS_SECRET_ACCESS_KEY;

  constructor(url: string) {
    this.url = url;
    this.sqs = new SQS({
      region,

      credentials: {
        accessKeyId: this.accessKey!,
        secretAccessKey: this.secretKey!,
      },
    });
  }
}

export default SQSClient;
