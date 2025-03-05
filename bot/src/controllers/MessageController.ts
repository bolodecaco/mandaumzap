import express from "express";
import SessionService from "../services/SessionService";
import { GlobalException } from "../exception/GlobalException";

const messageRouter = (sessionService: SessionService) => {
  const router = express.Router();

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

    try {
      const result = await sessionService.sendText({
        header: { receivers, sessionId, userId, messageId },
        text,
      });

      if (!result.wasSent && result.error) {
        return next(
          new GlobalException({
            message: result.error.message,
            statusCode: result.error.statusCode,
            details: result.error.details,
          })
        );
      }

      return res.status(200).json({ message: "Enviando mensagem" });
    } catch (error) {
      next(error);
    }
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

    try {
      const result = await sessionService.sendImage({
        header: { receivers, sessionId, userId, messageId },
        url,
        text: text || "",
      });

      if (!result.wasSent && result.error) {
        return next(
          new GlobalException({
            message: result.error.message,
            statusCode: result.error.statusCode,
            details: result.error.details,
          })
        );
      }

      return res.status(200).json({ message: "Enviando mensagem" });
    } catch (error) {
      next(error);
    }
  });

  router.post("/messages/send/video", async (req, res, next): Promise<any> => {
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

    try {
      const result = await sessionService.sendVideo({
        header: { receivers, sessionId, userId, messageId },
        url,
        text: text || "",
      });

      if (!result.wasSent && result.error) {
        return next(
          new GlobalException({
            message: result.error.message,
            statusCode: result.error.statusCode,
            details: result.error.details,
          })
        );
      }

      return res.status(200).json({ message: "Enviando mensagem" });
    } catch (error) {
      next(error);
    }
  });

  router.post("/messages/send/poll", async (req, res, next): Promise<any> => {
    const {
      sessionId,
      name,
      values,
      selectableCount,
      receivers,
      userId,
      messageId,
    } = req.body;
    if (!sessionId || !name || !values || !receivers || !userId || !messageId) {
      return next(
        new GlobalException({
          message: "Parâmetros inválidos",
          statusCode: 400,
          details: "Sessão, nome, opções ou destinatários não informados",
        })
      );
    }

    try {
      const result = await sessionService.sendPoll({
        header: { receivers, sessionId, userId, messageId },
        name,
        values,
        selectableCount,
      });

      if (!result.wasSent && result.error) {
        return next(
          new GlobalException({
            message: result.error.message,
            statusCode: result.error.statusCode,
            details: result.error.details,
          })
        );
      }

      return res.status(200).json({ message: "Enviando mensagem" });
    } catch (error) {
      next(error);
    }
  });

  return router;
};

export default messageRouter;
