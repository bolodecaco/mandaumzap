import app from "./src/services/ServerHttp";
import { port } from "./src/utils/global";

app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
