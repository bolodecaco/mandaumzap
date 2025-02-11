import express from "express";
import SessionService from "../services/SessionService";
import { GlobalException } from "../exception/GlobalException";

const sessionRouter = (sessionService: SessionService) => {
  const router = express.Router();

  router.get("/sessions/:userId", async (req, res): Promise<any> => {
    const { userId } = req.params;
    if (!userId) {
      throw new GlobalException({
        message: "Parâmetro inválido",
        statusCode: 400,
        details: "ID do usuário é obrigatório"
      });
    }
    const sessions = await sessionService.getAll(userId);
    return res.status(200).json(sessions);
  });

  router.post("/sessions", async (req, res): Promise<any> => {
    const { sessionId, userId } = req.body;
    if (!sessionId || !userId) {
      throw new GlobalException({
        message: "Parâmetros inválidos",
        statusCode: 400,
        details: "ID da sessão e ID do usuário são obrigatórios"
      });
    }
    
    if (sessionService.haveSession(sessionId)) {
      throw new GlobalException({
        message: "Sessão já existe",
        statusCode: 400,
        details: "Já existe uma sessão com este ID"
      });
    }

    const { qrcode, error } = await sessionService.connectSession({
      sessionId,
      userId,
    });

    if (error) {
      throw new GlobalException({
        message: "Erro na conexão",
        statusCode: 400,
        details: error
      });
    }

    if (qrcode === "") {
      return res.status(200).json({ message: "Iniciando sessão" });
    }
    return res.status(201).json({ qrcode });
  });

  router.delete("/sessions/close/:userId/:sessionId", async (req, res): Promise<any> => {
    const { sessionId, userId } = req.params;
    if (!sessionId) {
      throw new GlobalException({
        message: "Parâmetro inválido",
        statusCode: 400,
        details: "ID da sessão é obrigatório"
      });
    }

    if (!sessionService.haveSession(sessionId)) {
      throw new GlobalException({
        message: "Sessão não encontrada",
        statusCode: 400,
        details: "Não existe uma sessão com este ID"
      });
    }

    sessionService.closeSession({ sessionId, userId });
    return res.status(200).json({ message: "Sessão fechada" });
  });

  router.delete("/sessions/:userId/:sessionId", async (req, res): Promise<any> => {
    const { sessionId, userId } = req.params;
    if (!sessionId) {
      throw new GlobalException({
        message: "Parâmetro inválido",
        statusCode: 400,
        details: "ID da sessão é obrigatório"
      });
    }

    if (!sessionService.haveSession(sessionId)) {
      throw new GlobalException({
        message: "Sessão não encontrada",
        statusCode: 400,
        details: "Não existe uma sessão com este ID"
      });
    }

    sessionService.deleteSession({ sessionId, userId });
    return res.status(200).json({ message: "Sessão excluída" });
  });

  return router;
};

export default sessionRouter;
