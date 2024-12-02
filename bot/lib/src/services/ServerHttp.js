"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const connect_timeout_1 = __importDefault(require("connect-timeout"));
const dotenv_1 = __importDefault(require("dotenv"));
const SessionController_1 = __importDefault(require("../controllers/SessionController"));
const swagger_jsdoc_1 = __importDefault(require("swagger-jsdoc"));
const swagger_ui_express_1 = __importDefault(require("swagger-ui-express"));
const global_1 = require("../utils/global");
const app = (0, express_1.default)();
dotenv_1.default.config();
const specs = (0, swagger_jsdoc_1.default)(global_1.swaggerOptions);
app.use("/api/docs", swagger_ui_express_1.default.serve, swagger_ui_express_1.default.setup(specs));
app.use((req, res, next) => {
    const token = req.query.token;
    const expectedToken = process.env.TOKEN;
    if (!token)
        return res.status(401).json({ error: "Unauthorized" });
    if (token !== expectedToken)
        return res.status(403).json({ error: "Forbidden" });
    next();
});
app.use(express_1.default.json());
app.use((0, connect_timeout_1.default)("3s"));
app.use("/api", SessionController_1.default);
app.use((req, res, next) => {
    if (!req.timedout)
        next();
});
app.all("*", (req, res) => {
    return res.status(404).json({ error: "Not Found" });
});
exports.default = app;
//# sourceMappingURL=ServerHttp.js.map