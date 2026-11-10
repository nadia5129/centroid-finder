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
What URL and method is it?
- It's a POST because it updates the server: it creates a new job (new jobId), starts the JAR, and makes a new CSV
- You can't start it by pasting the URL in a browser, because that always sends a GET

What does the client send?
- The video filename in the URL path (req.params.filename)
- targetColor and threshold in the query string after the ? (req.query.targetColor, req.query.threshold)
- The client picks the color and threshold. ff0000 and 120 on the API page are just examples

What does the server send back when it works, and when it fails?
- 202 Accepted with {"jobId": "..."}: it means "got it, working on it", not "done"
- It sends the 202 right away, because the JAR takes about a minute and the request can't wait
- The client uses the jobId to check on it with GET /process/{jobId}/status
- 400 if targetColor or threshold is missing (the client's fault)
- 500 if the server can't start the job, like a wrong JAR path (the server's fault)

Does it need the JAR, ffmpeg, or just files and folders?
- It needs the JAR, started in the background (detached, ignore stdio, unref)
- inputPath = videos folder from .env + the filename from the URL
- outputCsv = results folder from .env + a name the server picks (video name or jobId?)
- targetColor and threshold come from the query string
- It also needs the JAR's path from .env



### GET /process/{jobId}/status
What URL and method is it?
- It's a GET, reads the information and doesn't change anything on the server

What does the client send?
- only job ID, in the URL path, it's the jobId the server returned from POST /process. there's no request body and no query string

What does the server send back when it works, and when it fails?
- 200 with "processing" the JAR is still running
- 200 with "done" and a "result" path to the CSV
- 200 with "error" and an error message
- a failed job is still a 200 because the status request itself worked
- 404: the server has never heard of that job ID.
- 500: the server itself broke while trying to look up the job, for example it couldn't read its job storage.

Does it need the JAR, ffmpeg, or just files and folders?
- neither. The JAR was already started by POST /process.
- the server just looks up the job's saved status, which updates when the JAR exits (0 = done, anything else = error)





### Videos at /videos/VIDEO_NAME
What URL and method is it?
- It's a GET, like /videos/intro.mp4. It just gives back the video file, so the client can play it.
- It's not one of the 4 endpoints. The spec just says every video from /api/videos can be found here.

What does the client send?
- only the video name in the URL path

What does the server send back when it works, and when it fails?
- the video file itself (not JSON)
- if the file isn't there, a 404

Does it need the JAR, ffmpeg, or just files and folders?
- just the videos folder. We don't write a route for it, express.static shares the whole folder in one line.


## Questions
<!-- Things we don't understand yet, or want to ask the professor -->
- The jobs are shared in Neon, but each CSV is saved on the laptop that ran the JAR. If my partner looks up my job, it says done, but their server doesn't have the file. Is that OK?


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
We chose a database: Neon (PostgreSQL in the cloud).

Scalability: if we had lots of jobs or users, a database can search and update them safely. Files would get messy.

Jobs survive a restart: if the server restarts, the jobs are still saved, so /status still works. A Map in memory would forget them.

We all share one database: Neon runs in the cloud, so the 3 of us connect to the same database and nobody has to install anything.

Downsides: it needs internet, the connection string is a password so it must stay out of git, and it's more to learn than a Map or files.

### Thumbnail: ffmpeg or our JAR?
We chose ffmpeg.

Speed: the frontend will probably show a thumbnail for every video. Starting our big JAR each time would take a second or more per video, and ffmpeg is almost instant.

The JAR keeps one job: our JAR already does one thing (video in → CSV out). Adding a thumbnail mode would mean changing how it reads its arguments and writing more tests, just to grab one frame.

Easy to install: we all have Macs with Homebrew, so it's just brew install ffmpeg. We'll add that to the README.

Downside: if someone hasn't installed ffmpeg, thumbnails won't work, so the server should send a clear 500 error when that happens.


## Plan
<!-- How each endpoint will work, step by step. What goes in .env? Where do results go? Who is doing what? -->

### Database (Neon)
Setting it up (one person does this):
1. Make a Neon project at neon.tech
2. Copy the connection string (keep the ?sslmode=require at the end)
3. Make the jobs table in Neon's SQL Editor, and save the same CREATE TABLE in server/schema.sql
4. Send the connection string to the team privately, never on GitHub

The jobs table (one row per job):
- id: the jobId from uuid
- filename: the video, like intro.mp4
- targetColor and threshold: what the client sent
- status: processing, done, or error
- result: the path to the CSV (empty until done)
- error: the error message (empty unless it failed)

How the endpoints use it:
- POST /process: add a row with status processing, start the JAR, send back the id
- when the JAR exits: update the row to done (exit code 0) or error (anything else)
- GET /process/{jobId}/status: look up the row by id, 404 if there's no row

In the server: the pg package talks to Neon (npm install pg, npm install -D @types/pg)

Before anyone puts the connection string in .env:
- add .env to .gitignore
- git rm --cached .env (because .env is already committed)
- commit a .env.example with the names but no real values, like DATABASE_URL=

### Results folder and CSV name
- Each CSV is named after the jobId, like results/123e4567-....csv
- Why jobId and not the video name (the API example uses intro.mp4.csv): with the video name, two jobs on the same video with different colors would overwrite each other's CSV
- The CSV name matches the job's id in the jobs table, so it's easy to find a job's result

### What goes in .env
- DATABASE_URL: the Neon connection string


## Architecture diagram
<!-- Client → Express → JAR / ffmpeg / folders -->


## Testing plan
<!-- Unit tests + Postman checks -->

