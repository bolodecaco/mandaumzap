import express from "express";

const router = express.Router();

const sessions = new Map<string, string>();

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
  const sessionsIds = Array.from(sessions.keys());
  return res.json(sessionsIds);
});

export default router;
