import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

public class DistanceImageBinarizerTest {
    // A pretend distance tool. The distance between two colors is just the
    // difference between their numbers, so it's easy to predict.
    private static class FakeDistanceFinder implements ColorDistanceFinder {
        List<Integer> colorsSeen = new ArrayList<>();

        @Override
        public double distance(int colorA, int colorB) {
            colorsSeen.add(colorA);
            colorsSeen.add(colorB);
            return Math.abs(colorA - colorB);
        }
    }

    // Makes an image where every pixel is the same color.
    private static BufferedImage imageFilledWith(int width, int height, int color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, color);
            }
        }
        return image;
    }

    // If pixel is exactly the target color distance is 0. Less
    // than the threshold pixel should be white or 1.
    @Test
    void toBinaryArray_pixelMatchesTarget_returnsWhite() {
        // Arrange
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x000000);
        DistanceImageBinarizer binarizer =
            new DistanceImageBinarizer(new FakeDistanceFinder(), 0x000000, 10);

        // Act
        int[][] actual = binarizer.toBinaryArray(image);

        // Assert
        assertArrayEquals(new int[][] { {1} }, actual);
    }

    // The pixel is far from the target (distance 100, threshold 10),
    // so the pixel should be black (0).
    @Test
    void toBinaryArray_pixelFarFromTarget_returnsBlack() {
        // Arrange
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x000064); // 100
        DistanceImageBinarizer binarizer =
            new DistanceImageBinarizer(new FakeDistanceFinder(), 0x000000, 10);

        // Act
        int[][] actual = binarizer.toBinaryArray(image);

        // Assert
        assertArrayEquals(new int[][] { {0} }, actual);
    }

    // The README says "less than" the threshold, so a distance that EQUALS
    // the threshold is not close enough. Distance 10, threshold 10 -> black.
    @Test
    void toBinaryArray_distanceEqualsThreshold_returnsBlack() {
        // Arrange
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x00000A); // 10
        DistanceImageBinarizer binarizer =
            new DistanceImageBinarizer(new FakeDistanceFinder(), 0x000000, 10);

        // Act
        int[][] actual = binarizer.toBinaryArray(image);

        // Assert
        assertArrayEquals(new int[][] { {0} }, actual);
    }

    // ---------- toBufferedImage ----------

    // Turn a 2x2 grid of 1s and 0s into a picture. 1 is white, 0 is black.
    // getRGB gives back the see-through digits too, so we only compare the
    // last 6 digits.
    @Test
    void toBufferedImage_twoByTwo_paintsBlackAndWhiteCorrectly() {
        // Arrange
        int[][] grid = {
            {0, 1},
            {1, 0}
        };
        DistanceImageBinarizer binarizer =
            new DistanceImageBinarizer(new FakeDistanceFinder(), 0x000000, 10);

        // Act
        BufferedImage actual = binarizer.toBufferedImage(grid);

        // Assert
        assertEquals(0x000000, actual.getRGB(0, 0) & 0xFFFFFF);
        assertEquals(0xFFFFFF, actual.getRGB(1, 0) & 0xFFFFFF);
        assertEquals(0xFFFFFF, actual.getRGB(0, 1) & 0xFFFFFF);
        assertEquals(0x000000, actual.getRGB(1, 1) & 0xFFFFFF);
    }

    // A grid with 2 rows and 3 columns should become a picture that is
    // 3 wide and 2 tall (not the other way around). The white pixels
    // should land in the right spots.
    @Test
    void toBufferedImage_nonSquareGrid_hasCorrectSizeAndPixelPlacement() {
        // Arrange
        int[][] grid = {
            {1, 0, 0},
            {0, 0, 1}
        };
        DistanceImageBinarizer binarizer =
            new DistanceImageBinarizer(new FakeDistanceFinder(), 0x000000, 10);

        // Act
        BufferedImage actual = binarizer.toBufferedImage(grid);

        // Assert
        assertEquals(3, actual.getWidth());
        assertEquals(2, actual.getHeight());
        assertEquals(0xFFFFFF, actual.getRGB(0, 0) & 0xFFFFFF); // row 0, col 0
        assertEquals(0x000000, actual.getRGB(1, 0) & 0xFFFFFF); // row 0, col 1
        assertEquals(0xFFFFFF, actual.getRGB(2, 1) & 0xFFFFFF); // row 1, col 2
    }
    
    @Test
    void toBufferedImage_allZeros_everyPixelBlack() {
        // Arrange
        int[][] grid = {
            {0, 0, 0},
            {0, 0, 0}
        };
        DistanceImageBinarizer binarizer =
            new DistanceImageBinarizer(new FakeDistanceFinder(), 0x000000, 10);

        // Act
        BufferedImage actual = binarizer.toBufferedImage(grid);

        // Assert
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 3; x++) {
                assertEquals(0x000000, actual.getRGB(x, y) & 0xFFFFFF);
            }
        }
    }

    // A grid of all 1s should make a picture where every pixel is white.
    @Test
    void toBufferedImage_allOnes_everyPixelWhite() {
        // Arrange
        int[][] grid = {
            {1, 1, 1},
            {1, 1, 1}
        };
        DistanceImageBinarizer binarizer =
            new DistanceImageBinarizer(new FakeDistanceFinder(), 0x000000, 10);

        // Act
        BufferedImage actual = binarizer.toBufferedImage(grid);

        // Assert
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 3; x++) {
                assertEquals(0xFFFFFF, actual.getRGB(x, y) & 0xFFFFFF);
            }
        }
    }

    // The smallest possible grid, just one square, should still work.
    @Test
    void toBufferedImage_oneByOne_makesOnePixelImage() {
        // Arrange
        int[][] grid = { {1} };
        DistanceImageBinarizer binarizer =
            new DistanceImageBinarizer(new FakeDistanceFinder(), 0x000000, 10);

        // Act
        BufferedImage actual = binarizer.toBufferedImage(grid);

        // Assert
        assertEquals(1, actual.getWidth());
        assertEquals(1, actual.getHeight());
        assertEquals(0xFFFFFF, actual.getRGB(0, 0) & 0xFFFFFF);
    }
    // TODO tests to add (Wave 4 says MAKE SURE YOU MAKE THOROUGH UNIT TESTS):
    //
    // toBinaryArray: threshold edges
    // [ ] distance exactly equal to threshold -> black (0), since the rule is "less than"
    // [ ] distance one below threshold -> white (1)
    // [ ] threshold of 0 -> every pixel is black, even an exact match
    //
    // toBinaryArray: alpha channel (README hint)
    // [ ] image with TYPE_INT_ARGB and a pixel like 0xFF000000 -> FakeDistanceFinder's
    //     colorsSeen should hold 0x000000, not 0xFF000000 (alpha bits stripped)
    // [ ] the target color is what gets passed to distance() (check colorsSeen)
    //
    // toBinaryArray: shape and placement
    // [ ] non-square image (e.g. 3 wide, 2 tall) -> array is [2][3], not [3][2]
    // [ ] mixed image with white and black pixels -> each lands at [y][x]
    //     (getRGB takes (x, y), but the array is [row][col])
    // [ ] distance() is called once per pixel (colorsSeen size = 2 * width * height)
    //
    // toBufferedImage
    // [ ] all 0s -> every pixel is black
    // [ ] all 1s -> every pixel is white
    // [ ] 1x1 grid works
    //
    // Round trip
    // [ ] toBufferedImage(toBinaryArray(image)) gives a black-and-white version
    //     of the image with the right pixels white
    @Test
    void roundTrip_matchingPixelBecomesWhiteAndOthersBlack() {
        // Arrange
        BufferedImage image = imageFilledWith(2, 2, 0x000064); // far from target
        image.setRGB(0, 0, 0x000000); // the only match
        DistanceImageBinarizer binarizer =
            new DistanceImageBinarizer(new FakeDistanceFinder(), 0x000000, 10);

        // Act
        BufferedImage actual = binarizer.toBufferedImage(binarizer.toBinaryArray(image));

        // Assert
        assertEquals(0xFFFFFF, actual.getRGB(0, 0) & 0xFFFFFF);
        assertEquals(0x000000, actual.getRGB(1, 0) & 0xFFFFFF);
        assertEquals(0x000000, actual.getRGB(0, 1) & 0xFFFFFF);
        assertEquals(0x000000, actual.getRGB(1, 1) & 0xFFFFFF);
    }
}
