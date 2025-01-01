import express from "express";
import SessionService from "../services/SessionService";

const chatRouter = (sessionService: SessionService) => {
  const router = express.Router();
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
  return router;
};
export default chatRouter;
