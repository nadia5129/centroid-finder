import type { JobStore } from "./jobStore.js";
import { statusFromExitCode } from "./jarRunner.js";

// Runs when the JAR exits: saves the job as done (with the CSV's URL) or as an error.
export async function finishJob(store: JobStore, jobId: string, code: number | null): Promise<void> {
    if (statusFromExitCode(code) === "done") {
        // the URL the client can open, because express.static serves the results folder at /results
        await store.markDone(jobId, `/results/${jobId}.csv`);
    } else if (code === null) {
        await store.markError(jobId, "Error processing video: the JAR could not start");
    } else {
        await store.markError(jobId, `Error processing video: the JAR exited with code ${code}`);
    }
}
