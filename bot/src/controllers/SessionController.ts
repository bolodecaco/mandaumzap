import express from "express";
import SessionService from "../services/SessionService";

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
 *        - in: query
 *          name: token
 *          schema:
 *           type: string
 *           default: "token"
 *          required: true
 *          description: Token for authentication
 *     responses:
 *       200:
 *         description: Sucesso
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 message:
 *                   type: string
 *                   example: "Exemplo de resposta"
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
 *     summary: Cria uma nova sessão
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
 *                 example: "session_12345"
 *                 description: O ID único para a sessão a ser criada.
 *     responses:
 *       200:
 *         description: Sucesso
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
 *         description: Requisição inválida
 */
router.post("/sessions", async (req, res): Promise<any> => {
  const { sessionId } = req.body;
  if (sessionService.haveSession(sessionId))
    return res.status(400).json("Session already exists");
  const qrcode = await sessionService.connectSession(sessionId);
  return res.status(200).json({ qrcode });
});

/**
 * @swagger
 * /api/messages/send:
 *   post:
 *     tags: [Message]
 *     summary: Envia mensagens
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
 *                 example: "session_12345"
 *                 description: O ID da sessão que será usada para enviar a mensagem.
 *               text:
 *                 type: string
 *                 example: "Olá! Esta é uma mensagem de teste."
 *                 description: O conteúdo da mensagem a ser enviada.
 *               recipients:
 *                 type: array
 *                 items:
 *                   type: string
 *                   example: "+5511999999999"
 *                 description: Uma lista de números de telefone dos destinatários (no formato E.164).
 *     responses:
 *       200:
 *         description: Sucesso
 *         content:
 *           application/json:
 *             schema:
 *               type: string
 *               example: "Sending message"
 */
router.post("/messages/send", async (req, res): Promise<any> => {
  const { sessionId, text, recipients } = req.body;
  const result = await sessionService.sendText({ recipients, text, sessionId });
  return result
    ? res.status(200).json("Sending message")
    : res.status(400).json("Error sending message");
});

export default router;
