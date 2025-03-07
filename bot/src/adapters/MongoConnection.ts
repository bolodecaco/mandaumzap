import { AuthenticationCreds } from "@whiskeysockets/baileys";
import { MongoClient, Collection } from "mongodb";
import { Logger } from "pino";
import { ChatProps } from "../@types/ChatProps";
import { UserMongoProps } from "../@types/UserMongoProps";
import dotenv from 'dotenv'

dotenv.config({ override: true})

const {
  DB_URI = "mongodb://admin:pass@localhost:27017",
  DB_NAME = "mandaumzap",
} = process.env;


class MongoConnection {
  sessionId: string;
  chats!: Collection<Document>;
  sessions!: Collection<Document>;
  keys!: Collection<Document>;
  users!: Collection<Document>;
  logger: Logger;
  creds!: AuthenticationCreds;
  client: MongoClient;

  constructor({ logger, sessionId }: { logger: Logger; sessionId: string }) {
    this.sessionId = sessionId;
    this.client = new MongoClient(DB_URI);
    this.logger = logger;
  }

  async init() {
    const db = this.client.db(DB_NAME!);
    this.keys = db.collection("keys");
    this.sessions = db.collection("sessions");
    this.chats = db.collection("chats");
    this.users = db.collection("users");
    await Promise.all([
      this.keys.createIndex({ sessionId: 1 }, { unique: false }),
      this.sessions.createIndex({ sessionId: 1 }, { unique: true }),
      this.chats.createIndex({ sessionId: 1 }, { unique: true }),
      this.users.createIndex({ sessionId: 1, userId: 1 }, { unique: true }),
    ]);
  }

  async addUser(user: UserMongoProps) {
    try {
      await this.users.updateOne(
        {
          userId: user.userId,
          sessionId: user.sessionId,
        },
        {
          $setOnInsert: { sessionId: user.sessionId, userId: user.userId },
        },
        { upsert: true }
      );
    } catch (error: any) {
      this.logger.error(
        `Erro ao adicionar usuário para a sessão ${this.sessionId}: ${error.message}`
      );
      throw error;
    }
  }

  async getAllSessions(userId: string): Promise<string[]> {
    try {
      const sessions = await this.users.find({ userId }).toArray();
      return sessions.map((session: any) => session.sessionId);
    } catch (error: any) {
      this.logger.error(
        `Erro ao buscar sessões do usuário ${userId}: ${error.message}`
      );
      throw error;
    }
  }

  async deleteSessions(sessions: string[]) {
    try {
      await this.users.deleteMany({ sessionId: { $in: sessions } });
      await this.sessions.deleteMany({ sessionId: { $in: sessions } });
      await this.keys.deleteMany({ sessionId: { $in: sessions } });
      await this.chats.deleteMany({ sessionId: { $in: sessions } });
    } catch (error: any) {
      this.logger.error(
        `Erro ao remover sessões ${sessions}: ${error.message}`
      );
    }
  }

  async getSessionByUserId(userId: string) {
    try {
      const sessions = await this.users.find({ userId }).toArray();
      return {
        user: userId,
        sessions: sessions.map((session: any) => session.sessionId),
      };
    } catch (error: any) {
      this.logger.error(
        `Erro ao buscar usuário da sessão ${this.sessionId}: ${error.message}`
      );
      throw error;
    }
  }

  async getSession(user: UserMongoProps) {
    try {
      const userSession: any = await this.users.findOne({
        sessionId: user.sessionId,
      });
      if (!userSession) return { exists: false, allowed: true };
      if (userSession.userId !== user.userId)
        return { allowed: false, exists: true };
      return {
        allowed: true,
        exists: true,
      };
    } catch (error: any) {
      this.logger.error(
        `Erro ao buscar usuário da sessão ${this.sessionId}: ${error.message}`
      );
      throw error;
    }
  }

  async deleteUser(user: UserMongoProps) {
    try {
      await this.users.deleteOne({
        sessionId: user.sessionId,
        userId: user.userId,
      });
    } catch (error: any) {
      this.logger.error(
        `Erro ao remover usuário da sessão ${this.sessionId}: ${error.message}`
      );
      throw error;
    }
  }

  async deleteSession(user: UserMongoProps) {
    try {
      await this.users.deleteOne({ sessionId: user.sessionId });
      await this.sessions.deleteOne({ sessionId: user.sessionId });
      await this.keys.deleteMany({ sessionId: user.sessionId });
      await this.chats.deleteMany({ sessionId: user.sessionId });
    } catch (error: any) {
      this.logger.error(
        `Erro ao remover sessão ${this.sessionId}: ${error.message}`
      );
      throw error;
    }
  }

  async addChats(chats: ChatProps[]) {
    try {
      await this.chats.updateOne(
        { sessionId: this.sessionId },
        {
          $setOnInsert: { sessionId: this.sessionId },
          $push: { chats: { $each: chats } },
        },
        { upsert: true }
      );
      this.logger.info(`Chats adicionados para a sessão: ${this.sessionId}`);
    } catch (error: any) {
      this.logger.error(
        `Erro ao adicionar chats para a sessão ${this.sessionId}: ${error.message}`
      );
      throw error;
    }
  }

  async removeSession() {
    try {
      await this.users.deleteMany({ sessionId: this.sessionId });
      await this.sessions.deleteOne({ sessionId: this.sessionId });
      await this.keys.deleteMany({ sessionId: this.sessionId });
      await this.chats.deleteOne({ sessionId: this.sessionId });
    } catch (error) {}
  }

  async close() {
    try {
      await this.client.close();
      this.logger.info(`Conexão com o MongoDB fechada com sucesso.`);
    } catch (error: any) {
      this.logger.error(
        `Erro ao fechar a conexão com o MongoDB: ${error.message}`
      );
    }
  }

  async connectToMongo() {
    try {
      await this.client.connect();
    } catch (error) {
      throw error;
    }
  }

  async getChats(sessionId: string): Promise<ChatProps[]> {
    const session = await this.chats.findOne({
      sessionId,
    });
    if (session && "chats" in session) {
      return session.chats as ChatProps[];
    }
    return [];
  }
}

export default MongoConnection;
