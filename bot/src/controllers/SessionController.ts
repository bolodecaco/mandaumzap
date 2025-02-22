import express from "express";
import SessionService from "../services/SessionService";
import { GlobalException } from "../exception/GlobalException";

const sessionRouter = (sessionService: SessionService) => {
  const router = express.Router();

  router.get("/sessions/:userId", async (req, res, next): Promise<any> => {
    const { userId } = req.params;
    if (!userId) {
      return next(
        new GlobalException({
          message: "Parâmetro inválido",
          statusCode: 400,
          details: "ID do usuário é obrigatório",
        })
      );
    }
    try {
      const sessions = await sessionService.getAll(userId);
      return res.status(200).json(sessions);
    } catch (error) {
      next(error);
    }
  });

  router.post("/sessions", async (req, res, next): Promise<any> => {
    const { sessionId, userId } = req.body;
    if (!sessionId || !userId) {
      return next(
        new GlobalException({
          message: "Parâmetros inválidos",
          statusCode: 400,
          details: "ID da sessão e ID do usuário são obrigatórios",
        })
      );
    }

    if (sessionService.haveSession(sessionId)) {
      return next(
        new GlobalException({
          message: "Sessão já existe",
          statusCode: 400,
          details: "Já existe uma sessão com este ID",
        })
      );
    }

    try {
      const { qrcode, error, status } = await sessionService.connectSession({
        sessionId,
        userId,
      });

      if (error) {
        return next(
          new GlobalException({
            message: "Erro na conexão",
            statusCode: 400,
            details: error,
          })
        );
      }

      if (qrcode === "") {
        return res.status(200).json({ message: "Iniciando sessão", status });
      }
      return res.status(201).json({ qrcode, status });
    } catch (error) {
      next(error);
    }
  });

  router.delete(
    "/sessions/close/:userId/:sessionId",
    async (req, res, next): Promise<any> => {
      const { sessionId, userId } = req.params;
      if (!sessionId || !userId) {
        return next(
          new GlobalException({
            message: "Parâmetro inválido",
            statusCode: 400,
            details: "ID da sessão e ID do usuário são obrigatórios",
          })
        );
      }

      if (!sessionService.haveSession(sessionId)) {
        return next(
          new GlobalException({
            message: "Sessão não encontrada",
            statusCode: 400,
            details: "Não existe uma sessão com este ID",
          })
        );
      }

      try {
        sessionService.closeSession({ sessionId, userId });
        return res.status(200).json({ message: "Sessão fechada" });
      } catch (error) {
        next(error);
      }
    }
  );

  router.delete(
    "/sessions/:userId/:sessionId",
    async (req, res, next): Promise<any> => {
      const { sessionId, userId } = req.params;
      if (!sessionId || !userId) {
        return next(
          new GlobalException({
            message: "Parâmetro inválido",
            statusCode: 400,
            details: "ID da sessão e ID do usuário são obrigatórios",
          })
        );
      }
      try {
        sessionService.deleteSession({ sessionId, userId });
        return res.status(200).json({ message: "Sessão excluída" });
      } catch (error) {
        next(error);
      }
    }
  );

  return router;
};

export default sessionRouter;
