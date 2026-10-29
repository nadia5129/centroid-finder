package io.github.nadia5129.centroidfinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;
//	Represents a frame extracted from a video
import org.bytedeco.javacv.Frame;
//Converts a JavaCV frame into a Java image
import org.bytedeco.javacv.Java2DFrameConverter;
//Stores the image in a format your centroid finder understands
import java.awt.image.BufferedImage;
//Saves the image as a PNG
import javax.imageio.ImageIO;
//Represents the output file
import java.io.File;

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

            // Get the next image frame from the video
            Frame frame = grabber.grabImage();

            // Make sure we actually got a frame
            if (frame != null) {

            // Create a converter to turn the frame into a BufferedImage
            try (Java2DFrameConverter converter =
                    new Java2DFrameConverter()) {

                // Convert the video frame into a Java image
                BufferedImage image = converter.convert(frame);

                // Make sure the conversion worked
                if (image != null) {

                    // Save the image as a PNG file
                    ImageIO.write(
                        image,
                        "png",
                        new File("extractedFrame.png")
                    );

                    System.out.println("Frame saved successfully!");
                }
            }
        }


        }
    }
    
}
