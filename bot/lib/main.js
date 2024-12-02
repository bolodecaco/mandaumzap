"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const ServerHttp_1 = __importDefault(require("./src/services/ServerHttp"));
const global_1 = require("./src/utils/global");
ServerHttp_1.default.listen(global_1.port, () => {
    console.log(`Server is running on port ${global_1.port}`);
});
//# sourceMappingURL=main.js.map