# Server Plan

API spec: https://github.com/auberonedu/salamander-api

## Notes on the API
<!-- For each endpoint: what does the client send? what does the server send back (success + errors)? does it need the JAR, ffmpeg, or just files/folders? -->

### GET /api/videos


### GET /thumbnail/{filename}


### POST /process/{filename}?targetColor=...&threshold=...


### GET /process/{jobId}/status


### Videos at /videos/VIDEO_NAME


## Questions
<!-- Things we don't understand yet, or want to ask the professor -->


## Decisions
<!-- What we chose and WHY -->

### Express + TypeScript


### Tracking jobs: database or files?


### Thumbnail: ffmpeg or our JAR?


## Plan
<!-- How each endpoint will work, step by step. What goes in .env? Where do results go? Who is doing what? -->


## Architecture diagram
<!-- Client → Express → JAR / ffmpeg / folders -->


## Testing plan
<!-- Unit tests + Postman checks -->

