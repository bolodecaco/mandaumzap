import express, { Request, Response, NextFunction } from "express";
import dotenv from "dotenv";
import sessionRouter from "../controllers/SessionController";
import chatRouter from "../controllers/ChatController";
import messageRouter from "../controllers/MessageController";
import swaggerJSDoc from "swagger-jsdoc";
import swaggerUi from "swagger-ui-express";
import { swaggerOptions } from "../utils/global";
import SessionService from "./SessionService";

const app = express();
dotenv.config();

const sessionService = new SessionService();

const specs = swaggerJSDoc(swaggerOptions);
app.use("/api/docs", swaggerUi.serve, swaggerUi.setup(specs));

app.use((req: Request, res: Response, next: NextFunction): any => {
  const token = req.query.token;
  const expectedToken = process.env.TOKEN;
  if (!token) return res.status(401).json({ error: "Unauthorized" });
  if (token !== expectedToken)
    return res.status(403).json({ error: "Forbidden" });
  next();
});

app.use(express.json());

app.use((req: Request, res: Response, next: NextFunction): void => {
  const timer = setTimeout(() => {
    if (!res.headersSent) {
      res.status(503).json({ error: "Request timed out" });
    }
  }, 40 * 1000);
  res.on("finish", () => clearTimeout(timer));
  res.on("close", () => clearTimeout(timer));
  next();
});

app.use("/api", sessionRouter(sessionService));
app.use("/api", chatRouter(sessionService));
app.use("/api", messageRouter(sessionService));

app.use((req: Request, res: Response, next: NextFunction): any => {
  if (!req.timedout) next();
});

app.all("*", (req: Request, res: Response): any => {
  return res.status(404).json({ error: "Not Found" });
});

export default app;
