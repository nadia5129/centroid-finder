import path from "path";
import dotenv from "dotenv";
import { createApp } from "./app.js";

dotenv.config({path: path.join(import.meta.dirname,"..","..",".env")});

const port = process.env.PORT || 3000;
const app = createApp();

app.listen(port, () =>{
    console.log(`Server running at http://localhost:${port} `);
});