import express from "express";
import SessionService from "../services/SessionService";
import { ChatProps } from "../@types/ChatProps";

const router = express.Router();

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
router.get("/sessions", (req, res): any => {
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
router.post("/sessions", async (req, res): Promise<any> => {
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
 * /api/messages/send/text:
 *   post:
 *     tags: [Message]
 *     summary: Envia mensagens de texto
 *     description: Envia uma mensagem de texto para os destinatários especificados usando uma sessão ativa.
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
 *                 description: O ID da sessão que será usada para enviar a mensagem.
 *               text:
 *                 type: string
 *                 example: "Olá! Esta é uma mensagem de teste."
 *                 description: O conteúdo da mensagem a ser enviada.
 *               receivers:
 *                 type: array
 *                 items:
 *                   type: string
 *                 example:
 *                   - "5511999999999@s.whatsapp.net"
 *                   - "31636136171888171313@g.us"
 *                 description: Uma lista de números de telefone dos destinatários (no formato E.164).
 *     responses:
 *       200:
 *         description: Sucesso
 *         content:
 *           application/json:
 *             schema:
 *               type: string
 *               example: "Enviando mensagem"
 *       400:
 *        description: Erro ao enviar a mensagem
 */
router.post("/messages/send/text", async (req, res): Promise<any> => {
  const { sessionId, text, receivers } = req.body;
  const result = await sessionService.sendText({ receivers, text, sessionId });
  return result
    ? res.status(200).json("Enviando mensagem")
    : res.status(400).json("Erro ao enviar a mensagem");
});

/**
 * @swagger
 * /api/chats/{sessionId}:
 *   get:
 *     tags: [Chat]
 *     summary: Lista de sessões
 *     description: Retorna uma lista com os IDs das sessões ativas
 *     parameters:
 *       - in: path
 *         name: sessionId
 *         schema:
 *           type: string
 *           default: "sessionId"
 *         required: true
 *         description: ID da sessão
 *       - in: query
 *         name: token
 *         schema:
 *           type: string
 *           default: "token"
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
 *                 example: "sessionId"
 */

router.get("/chats/:sessionId", async (req, res): Promise<any> => {
  const { sessionId } = req.params;
  const chats = await sessionService.getChats(sessionId);
  return res.status(200).json(chats);
});

export default router;
