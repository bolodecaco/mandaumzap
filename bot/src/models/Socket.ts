import {
  makeWASocket,
  Browsers,
  UserFacingSocketConfig,
  WASocket,
} from "@whiskeysockets/baileys";
import { pino } from "pino";
import MongoConnection from "../adapters/MongoConnection";

class WASocketWrapper {
  private sessionId: string;
  private socketOptions: Partial<UserFacingSocketConfig>;
  private logger: any;
  private socketInstance?: ReturnType<typeof makeWASocket>;

  constructor(sessionId: string) {
    this.sessionId = sessionId;
    this.logger = pino({ level: "fatal" });
    this.socketOptions = {
      browser: Browsers.macOS("Desktop"),
      logger: this.logger,
      syncFullHistory: false,
      version: [2, 3000, 1018540164],
    };
  }

  async initialize(): Promise<void> {
    const mongoConnection = new MongoConnection({
      logger: pino({ level: "fatal" }),
      sessionId: this.sessionId,
    });
    await mongoConnection.connectToMongo();
    await mongoConnection.init();
    const authState = await mongoConnection.useAuthState();
    this.socketInstance = makeWASocket({
      ...this.socketOptions,
      auth: {
        creds: authState.state.creds,
        keys: authState.state.keys,
      },
    });
    this.socketInstance.ev.on("creds.update", authState.saveState);
  }

  async removeSession(sessionId: string): Promise<void> {
    const mongoConnection = new MongoConnection({
      logger: pino({ level: "fatal" }),
      sessionId,
    });
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
