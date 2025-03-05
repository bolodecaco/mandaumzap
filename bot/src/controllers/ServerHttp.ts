import express, { Request, Response, NextFunction } from "express";
import dotenv from "dotenv";
import sessionRouter from "./SessionController";
import chatRouter from "./ChatController";
import messageRouter from "./MessageController";
import swaggerUi from "swagger-ui-express";
import YAML from "yamljs";
import SessionService from "../services/SessionService";

const app = express();
dotenv.config();

const sessionService = new SessionService();

const swaggerDocument = YAML.load("./src/docs/swagger.yaml");
app.use("/api/docs", swaggerUi.serve, swaggerUi.setup(swaggerDocument));

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

app.use((err: any, req: Request, res: Response, next: NextFunction) => {
  res.status(err.statusCode || 500).json({
    message: err.message || "Internal Server Error",
    details: err.details || "An unexpected error occurred"
  });
});

export default app;
