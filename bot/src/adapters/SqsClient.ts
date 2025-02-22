import AWS from "aws-sdk";
import dotenv from "dotenv";

dotenv.config();
const region = process.env.SQS_REGION;
AWS.config.update({ region });

class SQSClient {
  public readonly sqs: AWS.SQS;
  public readonly url: string;
  private accessKey = process.env.SQS_ACCESS_KEY_ID;
  private secretKey = process.env.SQS_SECRET_ACCESS_KEY;

  constructor(url: string) {
    this.url = url;
    this.sqs = new AWS.SQS({
      region,
      credentials: {
        accessKeyId: this.accessKey!,
        secretAccessKey: this.secretKey!,
      },
    });
  }
}

export default SQSClient;
