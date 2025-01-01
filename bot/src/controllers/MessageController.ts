import express from "express";
import SessionService from "../services/SessionService";

const messageRouter = (sessionService: SessionService) => {
  const router = express.Router();
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
    const result = await sessionService.sendText({
      receivers,
      text,
      sessionId,
    });
    return result
      ? res.status(200).json("Enviando mensagem")
      : res.status(400).json("Erro ao enviar a mensagem");
  });

  return router;
};
export default messageRouter;
