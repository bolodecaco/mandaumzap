import express from "express";
import SessionService from "../services/SessionService";

const chatRouter = (sessionService: SessionService) => {
  const router = express.Router();
  /**
   * @swagger
   * /api/chats/{userId}/{sessionId}:
   *   get:
   *     tags:
   *       - Chat
   *     summary: Lista de sessões
   *     description: Retorna uma lista com os IDs das sessões ativas
   *     parameters:
   *       - in: path
   *         name: userId
   *         schema:
   *           type: string
   *           default: "userId"
   *         required: true
   *         description: ID de um usuário
   *       - in: path
   *         name: sessionId
   *         schema:
   *           type: string
   *           default: "session"
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
   *                 example: sessionId
   *       404:
   *         description: Sessão não encontrada
   *         content:
   *           application/json:
   *             schema:
   *               type: object
   *               properties:
   *                 message:
   *                   type: string
   *                   example: Sessão não encontrada
   */
  router.get("/chats/:userId/:sessionId", async (req, res): Promise<any> => {
    const { sessionId, userId } = req.params;
    if (!sessionId || !userId)
      return res
        .status(400)
        .end(JSON.stringify("Parâmetros inválidos ou inexistentes"));
    const chats = await sessionService.getChats({ sessionId, userId });
    return res.status(200).json(chats);
  });
  return router;
};
export default chatRouter;
