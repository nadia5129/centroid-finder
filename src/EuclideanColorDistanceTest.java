import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EuclideanColorDistanceTest {

     //Teest same colors/both colors red
    @Test
    void distance_sameColor_returnsZero() {
        // Arrange
        int colorA = 0xFF0000;
        int colorB = 0xFF0000;

        EuclideanColorDistance finder = new EuclideanColorDistance();

        // Act
        double actual = finder.distance(colorA, colorB);

        // Assert
        assertEquals(0.0, actual, 0.000001);
    }

    //Test two different colors black and red
    @Test
    void distance_blackAndRed_returns255() {
        // Arrange
        int colorA = 0x000000;
        int colorB = 0xFF0000;

        EuclideanColorDistance finder = new EuclideanColorDistance();

        // Act
        double actual = finder.distance(colorA, colorB);

        // Assert
        assertEquals(255.0, actual, 0.000001);
    }
}
    

