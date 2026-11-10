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

### GET /thumbnail/{filename}
What URL and method is it?
- It's a GET because it only reads. It doesn't do anything except get the first frame of the video and show it.
- From the client's point of view, no job starts and no file gets added (even if our server makes a temporary JPG).
- Because it's a GET, you can paste the URL right into a browser and see the image, which is exactly what a thumbnail should do.
What does the client send?
- The video name in the URL path, asking the server to show us the first frame of that video.
What does the server send back when it works, and when it fails?
- When it works it sends 200 OK with the JPEG image itself (not JSON), with the header Content-Type: image/jpeg
- When it fails it sends a 500 error code
- {"error": "Error generating thumbnail"}

### POST /process/{filename}?targetColor=...&threshold=...
{filename} is a path parameter. The curly braces mean "a value goes here". It's part of the URL path itself, so:

GET /thumbnail/intro.mp4 → filename is intro.mp4
GET /thumbnail/demo.mov → filename is demo.mov
In Express you write the route as /thumbnail/:filename, with a colon instead of curly braces, and read it with req.params.filename.

How it differs from /api/videos: that one has no path parameter, because it's about all the videos. This one has to say which video.

Question 4: does it need the JAR, ffmpeg, or just files and folders?

In Express you write the route as /thumbnail/:filename, with a colon instead of curly braces, and read it with req.params.filename.



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

