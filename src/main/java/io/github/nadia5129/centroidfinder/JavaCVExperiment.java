package io.github.nadia5129.centroidfinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;

public class JavaCVExperiment {

    public static void main(String [] args) throws Exception{

        //sample of MP4 video - 
        String videoPath = "sampleInput/bright_balloon_sample_video.mp4";

        //The try statement automatically closes the grabber.
        // tool to read the video
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)){

            //open video
            grabber.start();

            // Print video information
            //getImageWidth() and getImageHeight() retrieve its dimensions.
            System.out.println("Width: " + grabber.getImageWidth());
            System.out.println("Height: " + grabber.getImageHeight());
            //getFrameRate() gets the reported frame rate.
            System.out.println("Frame rate: " + grabber.getFrameRate());

            // Convert microseconds into seconds
            //Dividing by 1_000_000.0 converts the duration into seconds.
            double duration =
                grabber.getLengthInTime() / 1_000_000.0;

            System.out.println("Duration: " + duration + " seconds");

        }
    }
    
}
