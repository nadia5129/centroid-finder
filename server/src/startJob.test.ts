import { describe, it, expect } from "vitest";
import { FakeJobStore } from "./fakeJobStore.js";
import { finishJob } from "./startJob.js";

describe("finishJob", () => {
    it("marks the job done with the result URL when the exit code is 0", async () => {
        // Arrange: a job that is still processing
        const store = new FakeJobStore();
        await store.createJob("job-1", "intro.mp4", "ff0000", 120);

        // Act: the JAR exited with 0
        await finishJob(store, "job-1", 0);

        // Assert
        const job = await store.findJob("job-1");
        expect(job?.status).toBe("done");
        expect(job?.result).toBe("/results/job-1.csv");
    });

    it("marks the job as an error when the exit code is 1", async () => {
        // Arrange: a job that is still processing
        const store = new FakeJobStore();
        await store.createJob("job-1", "intro.mp4", "ff0000", 120);

        // Act: the JAR exited with 1
        await finishJob(store, "job-1", 1);

        // Assert
        const job = await store.findJob("job-1");
        expect(job?.status).toBe("error");
        expect(job?.error).not.toBeNull();

    });

    it("marks the job as an error when the JAR couldn't start", async () => {
        // Arrange: a job that is still processing
        const store = new FakeJobStore();
        await store.createJob("job-1", "intro.mp4", "ff0000", 120);

        // Act: the JAR couldn't start
        await finishJob(store, "job-1", null);

        // Assert
        const job = await store.findJob("job-1");
        expect(job?.status).toBe("error");
        expect(job?.error).not.toBeNull();
    });
});
