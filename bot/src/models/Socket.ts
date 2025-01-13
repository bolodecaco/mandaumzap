import {
  makeWASocket,
  Browsers,
  UserFacingSocketConfig,
  WASocket,
} from "@whiskeysockets/baileys";
import { pino } from "pino";
import MongoConnection from "../adapters/MongoConnection";
import { ChatProps } from "../@types/ChatProps";
import { getWhatsappSocketVersion } from "../utils/functions";

class WASocketWrapper {
  private sessionId: string;
  private socketOptions: Partial<UserFacingSocketConfig>;
  private logger: any;
  private socketInstance?: ReturnType<typeof makeWASocket>;
  private static mongoConnection: MongoConnection | null = null;

  constructor(sessionId: string) {
    this.sessionId = sessionId;
    this.logger = pino({ level: "fatal" });
    this.socketOptions = {
      browser: Browsers.macOS("Desktop"),
      logger: this.logger,
      syncFullHistory: false,
    };
  }

  private async getMongoConnection(): Promise<MongoConnection> {
    if (
      !WASocketWrapper.mongoConnection ||
      WASocketWrapper.mongoConnection.sessionId !== this.sessionId
    ) {
      WASocketWrapper.mongoConnection = new MongoConnection({
        logger: this.logger,
        sessionId: this.sessionId,
      });
      await WASocketWrapper.mongoConnection.connectToMongo();
      await WASocketWrapper.mongoConnection.init();
    }
    return WASocketWrapper.mongoConnection;
  }

  async closeConnection(): Promise<void> {
    WASocketWrapper.mongoConnection?.close();
  }

  async addChats(chats: ChatProps[]): Promise<void> {
    const mongoConnection = await this.getMongoConnection();
    await mongoConnection.addChats(chats);
  }

  async getChats(): Promise<ChatProps[]> {
    const mongoConnection = await this.getMongoConnection();
    return await mongoConnection.getChats(this.sessionId);
  }

  async initialize(): Promise<void> {
    const mongoConnection = await this.getMongoConnection();
    const authState = await mongoConnection.useAuthState();
    this.socketInstance = makeWASocket({
      ...this.socketOptions,
      auth: {
        creds: authState.state.creds,
        keys: authState.state.keys,
      },
      version: await getWhatsappSocketVersion(),
    });
    this.socketInstance.ev.on("creds.update", authState.saveState);
  }

  async removeSession(): Promise<void> {
    const mongoConnection = await this.getMongoConnection();
    await mongoConnection.removeSession();
  }

  getSocket(): WASocket {
    if (!this.socketInstance) {
      throw new Error("Socket not initialized. Call `initialize` first.");
    }
    return this.socketInstance;
  }
}

export default WASocketWrapper;
