import type { Job, JobStore } from "./jobStore.js";

// A pretend job store for tests. It keeps jobs in a Map instead of Neon,
// so tests are fast and don't put test jobs in our shared database.
export class FakeJobStore implements JobStore {
    // jobId → job
    jobs = new Map<string, Job>();

    async createJob(id: string, filename: string, targetColor: string, threshold: number): Promise<void> {
        // every new job starts as processing, with no result or error yet
        this.jobs.set(id, {
            id,
            filename,
            targetColor,
            threshold,
            status: "processing",
            result: null,
            error: null,
        });
    }

    async markDone(id: string, resultPath: string): Promise<void> {
        const job = this.jobs.get(id);
        if (job) {
            job.status = "done";
            job.result = resultPath;
        }
    }

    async markError(id: string, message: string): Promise<void> {
        const job = this.jobs.get(id);
        if (job) {
            job.status = "error";
            job.error = message;
        }
    }

    async findJob(id: string): Promise<Job | undefined> {
        // undefined if there's no job with this id
        return this.jobs.get(id);
    }
}
