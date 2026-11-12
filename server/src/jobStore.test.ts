import { describe, it, expect } from "vitest";
import { FakeJobStore } from "./fakeJobStore.js";

describe("FakeJobStore", () => {
    it("starts a new job as processing with no result or error", async () => {
        // Arrange
        const store = new FakeJobStore();

        // Act
        await store.createJob("job-1", "intro.mp4", "ff0000", 120);
        const job = await store.findJob("job-1");

        // Assert
        expect(job).toEqual({
            id: "job-1",
            filename: "intro.mp4",
            targetColor: "ff0000",
            threshold: 120,
            status: "processing",
            result: null,
            error: null,
        });
    });

    it("marks a job as done with the CSV path", async () => {
        // Arrange
        const store = new FakeJobStore();
        await store.createJob("job-1", "intro.mp4", "ff0000", 120);
        // Act
        await store.markDone("job-1", "/results/job-1.csv");
        const job = await store.findJob("job-1");
        // Assert
        expect(job?.status).toBe("done");
        expect(job?.result).toBe("/results/job-1.csv");
    });

    it("marks a job as error with the message", async () => {
        // Arrange
        const store = new FakeJobStore();
        await store.createJob("job-1", "intro.mp4", "ff0000", 120);
        // Act
        await store.markError("job-1", "Error processing video");
        const job = await store.findJob("job-1");
        // Assert
        expect(job?.status).toBe("error");
        expect(job?.error).toBe("Error processing video");
    });

    it("returns undefined for a job that doesn't exist", async () => {
        // Arrange
        const store = new FakeJobStore();
        // Act
        const job = await store.findJob("not-a-real-id");
        // Assert
        expect(job).toBeUndefined();
    });
});
