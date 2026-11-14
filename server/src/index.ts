import fs from "fs";
import path from "path";
import dotenv from "dotenv";
import { createApp } from "./app.js";

dotenv.config({path: path.join(import.meta.dirname,"..","..",".env")});

const videoDir = process.env.VIDEOS_DIR;
const resultsDir = process.env.RESULTS_DIR;

//Stop right away if .env is missing a folder
if(!videoDir || !resultsDir){
    console.error("VIDEOS_DIR and RESULTS_DIR must be set in .env");
    process.exit(1);
}
//Make the results folder if it isn't there yet
fs.mkdirSync(resultsDir, {recursive: true});


const port = process.env.PORT || 3000;
const app = createApp({videoDir, resultsDir});

app.listen(port, () =>{
    console.log(`Server running at http://localhost:${port} `);
});