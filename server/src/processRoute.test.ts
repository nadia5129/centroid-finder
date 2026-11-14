import { describe, it, expect } from "vitest";
import request from "supertest";
import { createApp, type AppConfig } from "./app.js";
import { FakeJobStore } from "./fakeJobStore.js";

// Settings for a test app: fake folders, Nadia's fake job store, and a runner that does nothing
function makeConfig(store: FakeJobStore): AppConfig {
    return {
        videoDir: "/videos",
        resultsDir: "/results-folder",
        jarPath: "/jars/videoprocessor.jar",
        store,
        run: () => {},
    };
}

describe("POST /process/:filename", () => {
    it("sends 202 with a jobId and saves the job as processing", async () => {
        // Arrange
        const store = new FakeJobStore();
        const app = createApp(makeConfig(store));

        // Act: supertest sends a pretend request to our app (no real server needed)
        const response = await request(app).post("/process/intro.mp4?targetColor=ff0000&threshold=120");

        // Assert
        expect(response.status).toBe(202);
        const job = await store.findJob(response.body.jobId);
        expect(job?.status).toBe("processing");
    });

    it("sends 400 when targetColor is missing", async () => {
        // Arrange
        const store = new FakeJobStore();
        const app = createApp(makeConfig(store));

        // Act
        const response = await request(app).post("/process/intro.mp4?threshold=120");

        // Assert
        expect(response.status).toBe(400);
        expect(response.body).toEqual({ error: "Missing targetColor or threshold query parameter." });
    });

    it("sends 400 when threshold is missing", async () => {
        // Arrange
        const store = new FakeJobStore();
        const app = createApp(makeConfig(store));

        // Act
        const response = await request(app).post("/process/intro.mp4?targetColor=ff0000");

        // Assert
        expect(response.status).toBe(400);
        expect(response.body).toEqual({ error: "Missing targetColor or threshold query parameter." });
    });

    it("sends 500 when the job can't be started", async () => {
        // Arrange: a job store that always fails, like when Neon is down
        class BrokenJobStore extends FakeJobStore {
            async createJob(): Promise<void> {
                throw new Error("database is down");
            }
        }
        const app = createApp(makeConfig(new BrokenJobStore()));

        // Act
        const response = await request(app).post("/process/intro.mp4?targetColor=ff0000&threshold=120");

        // Assert
        expect(response.status).toBe(500);
        expect(response.body).toEqual({ error: "Error starting job" });
    });
});
