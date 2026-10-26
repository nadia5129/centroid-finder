import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
