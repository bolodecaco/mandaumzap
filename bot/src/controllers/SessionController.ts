import express from "express";
import SessionService from "../services/SessionService";

const sessionRouter = express.Router();

const sessionService = new SessionService();

/**
 * @swagger
 * /api/sessions:
 *   get:
 *     tags: [Session]
 *     summary: Lista de sessões
 *     description: Retorna uma lista com os IDs das sessões ativas
 *     parameters:
 *       - in: query
 *         name: token
 *         schema:
 *           type: string
 *           default: "token"
 *         required: true
 *         description: Token for authentication
 *     responses:
 *       200:
 *         description: Sucesso
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 type: string
 *                 example: "sessionId"
 */
sessionRouter.get("/sessions", (req, res): any => {
  const sessionsIds = Array.from(sessionService.getAll());
  return res.status(200).json(sessionsIds);
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
 *     responses:
 *       200:
 *         description: Sessão já existe e será inicializada
 *       201:
 *         description: Session criada com sucesso
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
sessionRouter.post("/sessions", async (req, res): Promise<any> => {
  const { sessionId } = req.body;
  if (sessionService.haveSession(sessionId))
    return res.status(400).json("A sessão já existe");
  const qrcode = await sessionService.connectSession(sessionId);
  return qrcode === ""
    ? res.status(200).json("Iniciando sessão")
    : res.status(201).json({ qrcode });
});

/**
 * @swagger
 * /api/sessions/close:
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
 *                 description: O ID único da sessão a ser fechada.
 *     responses:
 *       200:
 *         description: Sessão fechada com sucesso.
 *       400:
 *         description: A sessão não existe.
 */
sessionRouter.delete("/sessions/close", async (req, res): Promise<any> => {
  const { sessionId } = req.body;
  if (!sessionService.haveSession(sessionId))
    return res.status(400).json("A sessão não existe");
  sessionService.closeSession(sessionId);
  return res.status(200).json("Sessão fechada");
});

/**
 * @swagger
 * /api/sessions/{sessionId}:
 *   delete:
 *     tags:
 *       - Session
 *     summary: Fecha uma sessão
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
sessionRouter.delete("/sessions/:sessionId", async (req, res): Promise<any> => {
  const { sessionId } = req.params;
  if (!sessionService.haveSession(sessionId))
    return res.status(400).json("A sessão não existe");
  sessionService.deleteSession(sessionId);
  return res.status(200).json("Sessão excluída");
});

export default sessionRouter;
