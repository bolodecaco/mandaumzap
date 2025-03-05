import app from "./src/controllers/ServerHttp";
import { port } from "./src/utils/global";

app.listen(port, async () => {});
