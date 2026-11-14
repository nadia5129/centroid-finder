import express, { type Express } from "express";

// The folders the app needs. index.ts reads them from .env.
export type AppConfig ={
    videoDir: string;
    resultsDir: string;
};

// Builds the app and its routes. index.ts is what starts it.
export function createApp(config: AppConfig): Express{
    const app = express();

    app.use("/videos", express.static(config.videoDir));
    app.use("/results", express.static(config.resultsDir));

    // Test route
    app.get("/", (req, res) => {
        res.send("Hello from the server!");
    });

    return app;
}