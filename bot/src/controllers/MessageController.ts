import express from "express";
import SessionService from "../services/SessionService";
import { GlobalException } from "../exception/GlobalException";

interface MessageProps {
  sessionId: string;
  text: string;
  receivers: string[];
  userId: string;
  messageId: string;
  url?: string;
}

const messageRouter = (sessionService: SessionService) => {
  const router = express.Router();
  const messages: MessageProps[] = [];

  router.post("/messages/send/text", async (req, res, next): Promise<any> => {
    const { sessionId, text, receivers, userId, messageId } = req.body;
    if (!sessionId || !userId || !text || !receivers || !messageId) {
      return next(
        new GlobalException({
          message: "Parâmetros inválidos",
          statusCode: 400,
          details: "Sessão, texto ou destinatários não informados",
        })
      );
    }
    messages.push({ sessionId, text, receivers, userId, messageId });
    return res.status(200).json({ message: "Enviando mensagem" });
  });

  router.post("/messages/send/image", async (req, res, next): Promise<any> => {
    const { sessionId, url, receivers, userId, text, messageId } = req.body;
    if (!sessionId || !url || !receivers || !messageId || !userId) {
      return next(
        new GlobalException({
          message: "Parâmetros inválidos",
          statusCode: 400,
          details: "Sessão, URL ou destinatários não informados",
        })
      );
    }
    messages.push({ sessionId, text, receivers, userId, messageId, url });
    return res.status(200).json({ message: "Enviando mensagem" });
  });

  setInterval(() => {
    if (!messages.length) return;
    const message = messages.shift();
    if (!message) return;
    const { sessionId, text, receivers, userId, url, messageId } = message;
    if (url)
      return sessionService.sendImage({
        header: { userId, messageId, receivers, sessionId },
        url,
        text,
      });
    return sessionService.sendText({
      header: { userId, messageId, receivers, sessionId },
      text,
    });
  }, 3000);

  return router;
};

export default messageRouter;
