import express from "express";
import SessionService from "../services/SessionService";
import { GlobalException } from "../exception/GlobalException";

const sessionRouter = (sessionService: SessionService) => {
  const router = express.Router();

  /**
   * @swagger
   * /api/sessions/{userId}:
   *   get:
   *     tags: [Session]
   *     summary: Lista de sessões
   *     description: Retorna uma lista com os IDs das sessões ativas
   *     parameters:
   *       - in: path
   *         name: userId
   *         schema:
   *           type: string
   *           example: "userId"
   *         required: true
   *         description: O ID único do usuário.
   *       - in: query
   *         name: token
   *         schema:
   *           type: string
   *           example: "token"
   *         required: true
   *         description: Token para autenticação
   *     responses:
   *       200:
   *         description: Sucesso
   *         content:
   *           application/json:
   *             schema:
   *               type: array
   *               items:
   *                 type: string
   *                 example: "sessionId12345"
   */
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

  /**
   * @swagger
   * /api/sessions:
   *   post:
   *     tags: [Session]
   *     summary: Cria ou inicializa uma sessão
   *     description: Cria uma nova sessão e retorna o QR Code para conexão.
   *     parameters:
   *       - in: query
   *         name: token
   *         schema:
   *           type: string
   *           default: "token"
   *         required: true
   *         description: Token para autenticação.
   *     requestBody:
   *       required: true
   *       content:
   *         application/json:
   *           schema:
   *             type: object
   *             properties:
   *               sessionId:
   *                 type: string
   *                 example: "session"
   *                 description: O ID único para a sessão a ser criada.
   *               userId:
   *                 type: string
   *                 example: "userId"
   *                 description: Token de autenticação para a sessão.
   *     responses:
   *       200:
   *         description: Sessão já existe e será inicializada
   *       201:
   *         description: >
   *           Session criada com sucesso
   *           OBS: Caso seja a primeira conexão, o userId deve ser undefined ou null.
   *         content:
   *           application/json:
   *             schema:
   *               type: object
   *               properties:
   *                 qrcode:
   *                   type: string
   *                   example: "https://example.com/qrcode.png"
   *                   description: URL do QR Code para a conexão.
   *       400:
   *         description: Requisição inválida, já existe uma sessão com o ID fornecido.
   */
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

  /**
   * @swagger
   * /api/sessions/close/{userId}/{sessionId}:
   *   delete:
   *     tags:
   *       - Session
   *     summary: Fecha uma sessão
   *     description: Fecha uma sessão ativa.
   *     parameters:
   *       - in: query
   *         name: token
   *         schema:
   *           type: string
   *           default: "token"
   *         required: true
   *         description: Token para autenticação.
   *       - in: path
   *         name: userId
   *         schema:
   *           type: string
   *           default: "userId"
   *         required: true
   *         description: O ID único do usuário.
   *       - in: path
   *         name: sessionId
   *         schema:
   *           type: string
   *           default: "session"
   *         required: true
   *         description: O ID único da sessão a ser fechada.
   *     responses:
   *       200:
   *         description: Sessão fechada com sucesso.
   *       400:
   *         description: A sessão não existe.
   */
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

  /**
   * @swagger
   * /api/sessions/{userId}/{sessionId}:
   *   delete:
   *     tags:
   *       - Session
   *     summary: Deleta e fecha uma sessão
   *     description: Fecha uma sessão ativa.
   *     parameters:
   *       - in: path
   *         name: sessionId
   *         schema:
   *           type: string
   *           default: "session"
   *         required: true
   *         description: O ID único da sessão a ser fechada.
   *       - in: query
   *         name: token
   *         schema:
   *           type: string
   *           default: "token"
   *         required: true
   *         description: Token para autenticação.
   *     responses:
   *       200:
   *         description: Sessão fechada com sucesso.
   *       400:
   *         description: A sessão não existe.
   */
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
