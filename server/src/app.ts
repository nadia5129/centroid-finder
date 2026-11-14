import fs from "fs/promises";
import path from "path";
import { execFile } from "child_process";
import express, { type Express } from "express";
import type { JobStore } from "./jobStore.js";
import type { runInBackground } from "./jarRunner.js";
import { processRoute } from "./processRoute.js";


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

    // GET /api/videos: list the video files in the videos folder
    app.get("/api/videos", async (req, res) => {
        try {
            const files = await fs.readdir(config.videoDir);
            const videos = files.filter((name) => {
                const lower = name.toLowerCase();
                return lower.endsWith(".mp4") || lower.endsWith(".mov");
            });
            res.json(videos);
        } catch (error) {
            console.error(error);
            res.status(500).json({ error: "Error reading video directory" });
        }
    });

    // GET /thumbnail/:filename: send back the video's first frame as a JPEG
    app.get("/thumbnail/:filename", (req, res) => {
        // basename keeps only the file name, so "../" can't reach other folders
        const videoPath = path.join(config.videoDir, path.basename(req.params.filename));

        // Ask ffmpeg for 1 frame as a JPEG, written to its output (pipe:1) instead of a file
        const args = ["-v", "error", "-i", videoPath, "-frames:v", "1", "-f", "image2", "-c:v", "mjpeg", "pipe:1"];

        execFile("ffmpeg", args, { encoding: "buffer", maxBuffer: 10 * 1024 * 1024 }, (error, stdout) => {
            if (error) {
                // no video, not a video, or ffmpeg isn't installed
                console.error(error.message);
                res.status(500).json({ error: "Error generating thumbnail" });
                return;
            }
            res.type("image/jpeg").send(stdout);
        });
    });

    // Test route
    app.get("/", (req, res) => {
        res.send("Hello from the server!");
    });

    return app;
}