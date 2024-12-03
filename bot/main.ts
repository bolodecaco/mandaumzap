import app from "./src/services/ServerHttp";
import { port } from "./src/utils/global";

app.listen(port, async () => {
  console.log(`Server is running on port ${port}`);
});
