import express from "express";
import SessionService from "../services/SessionService";
import { GlobalException } from "../exception/GlobalException";

const chatRouter = (sessionService: SessionService) => {
  const router = express.Router();

  router.get("/chats/:userId/:sessionId", async (req, res, next): Promise<any> => {
    const { sessionId, userId } = req.params;
    if (!sessionId || !userId) {
      return next(new GlobalException({
        message: "Parâmetros inválidos",
        statusCode: 400,
        details: "ID da sessão e ID do usuário são obrigatórios"
      }));
    }

    try {
      const chats = await sessionService.getChats({ sessionId, userId });
      if (!chats) {
        return next(new GlobalException({
          message: "Chats não encontrados",
          statusCode: 404,
          details: "Não foi possível recuperar os chats para esta sessão"
        }));
      }

      return res.status(200).json(chats);
    } catch (error) {
      next(error);
    }
  });

  return router;
};

export default chatRouter;
