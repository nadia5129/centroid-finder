import express, { type Express } from "express";

export function createApp(): Express{
    const app = express();

    app.get("/", (req, res) => {
        res.send("Hello from the server!");
    });
    
    return app;
}