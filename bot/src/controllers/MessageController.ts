import express from "express";
import SessionService from "../services/SessionService";
import { GlobalException } from "../exception/GlobalException";

const messageRouter = (sessionService: SessionService) => {
  const router = express.Router();

  router.post("/messages/send/text", async (req, res, next): Promise<any> => {
    const { sessionId, text, receivers, userId } = req.body;
    if (!sessionId || !text || !receivers) {
      return next(new GlobalException({
        message: "Parâmetros inválidos",
        statusCode: 400,
        details: "Sessão, texto ou destinatários não informados"
      }));
    }
    
    try {
      const result = await sessionService.sendText({
        header: { receivers, sessionId, userId },
        text,
      });
      
      if (!result) {
        return next(new GlobalException({
          message: "Erro ao enviar mensagem",
          statusCode: 400,
          details: "Não foi possível enviar a mensagem de texto"
        }));
      }
      
      return res.status(200).json({ message: "Enviando mensagem" });
    } catch (error) {
      next(error);
    }
  });

  router.post("/messages/send/image", async (req, res, next): Promise<any> => {
    const { sessionId, url, receivers, userId, text } = req.body;
    if (!sessionId || !url || !receivers) {
      return next(new GlobalException({
        message: "Parâmetros inválidos",
        statusCode: 400,
        details: "Sessão, URL ou destinatários não informados"
      }));
    }
    
    try {
      const result = await sessionService.sendImage({
        header: { receivers, sessionId, userId },
        url,
        text: text || "",
      });
      
      if (!result) {
        return next(new GlobalException({
          message: "Erro ao enviar imagem",
          statusCode: 400,
          details: "Não foi possível enviar a imagem"
        }));
      }
      
      return res.status(200).json({ message: "Enviando mensagem" });
    } catch (error) {
      next(error);
    }
  });

  router.post("/messages/send/video", async (req, res, next): Promise<any> => {
    const { sessionId, url, receivers, userId, text } = req.body;
    if (!sessionId || !url || !receivers) {
      return next(new GlobalException({
        message: "Parâmetros inválidos",
        statusCode: 400,
        details: "Sessão, URL ou destinatários não informados"
      }));
    }
    
    try {
      const result = await sessionService.sendVideo({
        header: { receivers, sessionId, userId },
        url,
        text: text || "",
      });
      
      if (!result) {
        return next(new GlobalException({
          message: "Erro ao enviar vídeo",
          statusCode: 400,
          details: "Não foi possível enviar o vídeo"
        }));
      }
      
      return res.status(200).json({ message: "Enviando mensagem" });
    } catch (error) {
      next(error);
    }
  });

  router.post("/messages/send/poll", async (req, res, next): Promise<any> => {
    const { sessionId, name, values, selectableCount, receivers, userId } = req.body;
    if (!sessionId || !name || !values || !receivers) {
      return next(new GlobalException({
        message: "Parâmetros inválidos",
        statusCode: 400,
        details: "Sessão, nome, opções ou destinatários não informados"
      }));
    }
    
    try {
      const result = await sessionService.sendPoll({
        header: { receivers, sessionId, userId },
        name,
        values,
        selectableCount,
      });
      
      if (!result) {
        return next(new GlobalException({
          message: "Erro ao enviar enquete",
          statusCode: 400,
          details: "Não foi possível enviar a enquete"
        }));
      }
      
      return res.status(200).json({ message: "Enviando mensagem" });
    } catch (error) {
      next(error);
    }
  });

  return router;
};

export default messageRouter;
