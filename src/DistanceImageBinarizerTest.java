import static org.junit.jupiter.api.Assertions.assertArrayEquals;

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

}
