import express, { Request, Response, NextFunction } from "express";
import timeout from "connect-timeout";
import dotenv from "dotenv";
import router from "../controllers/SessionController";
import swaggerJSDoc from "swagger-jsdoc";
import swaggerUi from "swagger-ui-express";
import { swaggerOptions } from "../utils/global";

const app = express();
dotenv.config();

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

app.use(timeout("3s"));

app.use("/api", router);

app.use((req: Request, res: Response, next: NextFunction): any => {
  if (!req.timedout) next();
});

app.all("*", (req: Request, res: Response): any => {
  return res.status(404).json({ error: "Not Found" });
});

export default app;
