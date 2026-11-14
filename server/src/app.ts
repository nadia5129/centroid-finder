import express, { type Express } from "express";
import type { JobStore } from "./jobStore.js";
import type { runInBackground } from "./jarRunner.js";
import { processRoute } from "./processRoute.js";
import {statusRoute} from "./statusRoute.js";


// The folders the app needs. index.ts reads them from .env.
export type AppConfig ={
    videoDir: string;
    resultsDir: string;
        jarPath: string;
    store: JobStore;
    // only tests set this, so they can use a fake instead of really starting java
    run?: typeof runInBackground;

};

// Builds the app and its routes. index.ts is what starts it.
export function createApp(config: AppConfig): Express{
    const app = express();

    app.use("/videos", express.static(config.videoDir));
    app.use("/results", express.static(config.resultsDir));
    app.use(processRoute(config));
    app.use(statusRoute(config));


    // Test route
    app.get("/", (req, res) => {
        res.send("Hello from the server!");
    });

    return app;
}