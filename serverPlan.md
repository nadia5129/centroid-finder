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
What URL and method is it?
- It's a GET because it only reads. It doesn't do anything except get the first frame of the video and show it.
- From the client's point of view, no job starts and no file gets added (even if our server makes a temporary JPG).
- Because it's a GET, you can paste the URL right into a browser and see the image, which is exactly what a thumbnail should do.
- {filename} is a path parameter. The curly braces mean "a value goes here". It's part of the URL path itself, so:
  - GET /thumbnail/intro.mp4 → filename is intro.mp4
  - GET /thumbnail/demo.mov → filename is demo.mov
- In Express you write the route as /thumbnail/:filename, with a colon instead of curly braces, and read it with req.params.filename.
What does the client send?
- The video name in the URL path, asking the server to show us the first frame of that video.
- How it differs from /api/videos: that one has no path parameter, because it's about all the videos. This one has to say which video.
What does the server send back when it works, and when it fails?
- When it works it sends 200 OK with the JPEG image itself (not JSON), with the header Content-Type: image/jpeg
- When it fails it sends a 500 error code
- {"error": "Error generating thumbnail"}
Does it need the JAR, ffmpeg, or just files and folders?
- We chose ffmpeg. It needs something that can open a video and grab the first frame, so the server runs one ffmpeg command instead of starting our JAR.

### POST /process/{filename}?targetColor=...&threshold=...


### GET /process/{jobId}/status
What URL and method is it?
- It's a GET, reads the information and doesn't change anything on the server

What does the client send?
- only job ID, in the URL path, it's the jobId the server returned from POST /process. there's no request body and no query string

What does the server send back when it works, and when it fails?
- 200 with "processing" the JAR is still running
- TODO: 200 with "done" ... (what else comes back with it?)
- TODO: 200 with "error" ... (what else comes back with it?)
- TODO: why is a failed job still a 200 and not a 500?
- 404: the server has never heard of that job ID.
- 500: the server itself broke while trying to look up the job, for example it couldn't read its job storage.

Does it need the JAR, ffmpeg, or just files and folders?
- neither. The JAR was already started by POST /process.
- TODO: so where does the status come from, and what updates it when the JAR finishes?





### Videos at /videos/VIDEO_NAME


## Questions
<!-- Things we don't understand yet, or want to ask the professor -->


## Decisions
<!-- What we chose and WHY -->

### Express + TypeScript
Why Express

- It's the default option in the assignment.
- We've already learned it in other classes, so the team doesn't have to learn a new framework.
- It has simple tools for everything the API needs: routes like /thumbnail/:filename, express.static for the videos and results folders, and JSON responses.

Why TypeScript

- We all wanted to learn TypeScript, and this project is a good chance to practice it.
- It catches mistakes before the code runs. For example, the job status can only be "processing", "done", or "error". TypeScript warns you if someone types "finished" by mistake.
- It's clearer. The types show what each function takes and returns.
- It's still JavaScript underneath, so all the same Node and Express code works.

The Downside 
- TypeScript is new for all of use and will be an extra setup


### Tracking jobs: database or files?


### Thumbnail: ffmpeg or our JAR?
We chose ffmpeg.

Speed: the frontend will probably show a thumbnail for every video. Starting our big JAR each time would take a second or more per video, and ffmpeg is almost instant.

The JAR keeps one job: our JAR already does one thing (video in → CSV out). Adding a thumbnail mode would mean changing how it reads its arguments and writing more tests, just to grab one frame.

Easy to install: we all have Macs with Homebrew, so it's just brew install ffmpeg. We'll add that to the README.

Downside: if someone hasn't installed ffmpeg, thumbnails won't work, so the server should send a clear 500 error when that happens.


## Plan
<!-- How each endpoint will work, step by step. What goes in .env? Where do results go? Who is doing what? -->


## Architecture diagram
<!-- Client → Express → JAR / ffmpeg / folders -->


## Testing plan
<!-- Unit tests + Postman checks -->

