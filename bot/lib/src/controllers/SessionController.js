"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const router = express_1.default.Router();
const sessions = new Map();
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
router.get("/sessions", (req, res) => {
    const sessionsIds = Array.from(sessions.keys());
    return res.json(sessionsIds);
});
exports.default = router;
//# sourceMappingURL=SessionController.js.map