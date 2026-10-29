# Video Library Options

## JavaCV
**Pros**
- Format: this engine is built on FFmpeg which is the same as YouTube's processing and other major video tools. It can also open almost any format and codec which means a random phone MP4 will very likely work. (which may be good for future reference/updates/add ons in our project)
- Fit with our code : It uses BufferedImage directly via Java2DFrameConverter. That's the same as our whole pipeline already uses, so integrating it is mostly writing a loop. 
- Time column : it gives us timestamps directly. So we don't need to worry about thinking on every frame number ourselves. Keeps us from needing to come up with the code for frame number and frame rate... already in this VL option.
- Setup : Setup would be with javacv-platform. We would only need to install it as a dependency in our pom.xml, and Maven would download it for us the next time someone runs mvn. What's nice is that only one of us needs to add it to the pom, and everyone else's Maven downloads it automatically. This one also allows many operating systems. Well in the ProcessBuilder option we would all need to download FFmpeg ourselves, but with this option we get it once we use mvn because it comes bundled with JavaCV.
- Speed : it's fast! the decoding happens in native C code rather than Java.
- Help available : Well the documentation is slim but because it's so popular there is much help on platforms like Stack Overflow and GitHub issues. It's actively maintained so bugs are fixed in a timely manner and it keeps working with new Java versions.

**Cons**
- Download size : The thing is that the size of the download is very substantial. It's huge! It's so huge to be available to many operating systems. Although because we all use Mac there is a work around: while doing the download we can have it only download what we need for Mac OS, but it may be a bit cumbersome.
- Debugging : Because it decodes in C the errors are also in C which means we will have a harder time at first glance understanding our errors. Sometimes a C error can even crash the whole program instead of throwing a Java exception, so a try/catch won't catch it.
- Units : The thing about this one is that JavaCV gives us timestamps in microseconds instead of normal seconds. So if we forget to convert, the numbers in our CSV could obviously be way off (1,000,000 times too big). The assignment also specifies that the first column has to be in number of seconds since the start of the video.
- Reused Frames : These frames get reused so for our pipeline we would need to process each frame immediately because the grabber and the converter recycle the same Frame and BufferedImage objects.
- Cleanup : For cleanup it's vital that we call stop() or close() or else it leaks native memory.
- Docs : Well the official documentation is thin, so we mostly learn from example code and clear answers will not be immediate.

## JCodec
**Pros**
- 

**Cons**
- 

## FFmpeg via ProcessBuilder
**Pros**
- 

**Cons**
- 

## Our choice
We chose ___ because ...
