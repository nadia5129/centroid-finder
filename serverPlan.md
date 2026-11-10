# Server Plan

API spec: https://github.com/auberonedu/salamander-api

## Notes on the API
<!-- For each endpoint: what does the client send? what does the server send back (success + errors)? does it need the JAR, ffmpeg, or just files/folders? -->

### GET /api/videos
What URL and method is it? 
- Get only reads the information and doesnt change the server.
What does the client send?
- Nothing, no file name . no query. Client only asking what videos do we have 
What does the server send back when it works, and when it fails?
- When it works it should send 200 ok with a json array of file names
- when it fails gives us a 500 error code 
- {"error": "Error reading video directory"}
Does it need the JAR, ffmpeg, or just files and folders?
- Just a folder, the server reads the list of files in the videos folder
- (nodes) its a specific class that node uses fs.readdir
- it doesnt need a jar or ffmpeg, simplised end point to test 

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

