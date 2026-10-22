This application takes three command-line arguments therefore we know what type of input to expect...

read command-line inputs → load an image → turn it into black and white → find white-pixel groups → save the results.

CSV = Comma-Separated Values
A CSV file is a plain text file that stores data in rows and columns.

For args.length < 3 It checks if the user entered fewer than three arguments. If they did, the program prints a message showing how to run the application and stops running.

The imports:
import java.awt.image.BufferedImage; -
 holds a picture in memory so the program can look at each pixels colors.

import java.io.File; -
points to a file on your computer like image.png

import java.io.PrintWriter; -
writes text into a file. Here it creates groups.csv

import java.util.List; -
A container that holds many items in order. Here it holds all the groups the program finds. 

import javax.imageio.ImageIO; -
opens an image file and saves an image file. It loads your input picture and saves binarized.png

TODO (nadia) line 57 -69

This part is where the program reads the input values from the command line and stores them in variables so the rest of the code can use them. The code:

String inputImagePath = args[0];
String hexTargetColor = args[1];
int threshold = 0;

This gives names to the first two arguments and declares a variable for the threshold. The image path selects the file the user wants to analyze, and the target color tells the program what color to look for. The threshold is the value used to decide how close a pixel needs to be to that target color before it is counted as part of the selection.

The code then tries to convert the third argument from text to an integer:

threshold = Integer.parseInt(args[2]);

The threshold arrives as text, so Integer.parseInt converts it to an integer. The try/catch handles input that is not a valid integer. On failure, the program prints an error and returns. The try block means "attempt this operation." If the conversion works, threshold gets the converted value and the program continues. If it does not work, it throws a NumberFormatException, and the catch block handles that problem by printing a clear message and stopping the program before it continues with invalid data.

TODO (jessica) line 71 -81

This section is where the program actually turns the image into a black-and-white version. First, it calls:

int[][] binaryArray = binarizer.toBinaryArray(inputImage);

This creates a two-dimensional array where each pixel is converted to either 1 or 0 depending on whether it is close enough to the target color. Then it does:

BufferedImage binaryImage = binarizer.toBufferedImage(binaryArray);

This turns the binary array back into an image so the program can save it as a real PNG file. The try/catch around ImageIO.write checks whether the file was saved correctly. If something goes wrong, it prints an error message instead of crashing the whole program.

TODO (paris) line 83-100

These lines are the part where the program turns the processed image into meaningful group data. First, it creates a group-finder object:

ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());
List<Group> groups = groupFinder.findConnectedGroups(inputImage);

This tells the program to use the same binarization rules to decide which pixels are white, and then find connected groups of those white pixels using depth-first search. A connected group is a cluster of touching white pixels. The result is stored in a List<Group>, so each group can be analyzed separately.

After that, the program writes those groups into a CSV file:

try (PrintWriter writer = new PrintWriter("groups.csv")) {
    for (Group group : groups) {
        writer.println(group.toCsvRow());
    }
}

This opens groups.csv and writes one line per group. Each line is a summary of that group, such as its size and centroid coordinates. This is important because the program is not only producing a binarized image; it is also converting the image data into a spreadsheet-friendly file that can be used for analysis or comparison.


CLASS RESPONSIBILITIES:

ImageSummaryApp.java - The main program that collects input, loads the image, binarizes it, finds groups, and saves output

DistanceImageBinarizer.java - This is the class that decides whether each pixel is white or black based on distance from the target color.

BinarizingImageGroupFinder.java - This class combines binarization and group finding into one workflow.

EuclideanColorDistance.java - This class combines binarization and group finding into one workflow

DfsBinaryGroupFinder.java - This is the actual connected-component search algorithm that finds groups of white pixels.

FLOW OF DATA : aka heart of project

1.Input image is loaded
2.Target color and threshold are read from the command line
3.Each pixel is compared to the target color.
4.Pixels under the threshold become white, others become black
5.White pixels are grouped if they touch - up/down/left/right
6.Each group is summarized as size and centroid
7. Results are saved as a PNG and a CSV