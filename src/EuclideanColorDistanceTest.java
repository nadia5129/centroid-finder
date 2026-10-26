import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EuclideanColorDistanceTest {

     //Test same colors/both colors red
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

    //Test black and white
    @Test
    void distance_blackAndWhite_returnsCorrectDistance() {
        // Arrange
        int colorA = 0x000000;
        int colorB = 0xFFFFFF;
        EuclideanColorDistance finder = new EuclideanColorDistance();

        // Act
        double actual = finder.distance(colorA, colorB);

        // Assert
        assertEquals(Math.sqrt(3 * 255 * 255), actual, 0.000001);
    }

    //Test Red and Green 
    @Test
    void distance_redAndGreen_returnsCorrectDistance() {
        // Arrange
        int colorA = 0xFF0000;
        int colorB = 0x00FF00;
        EuclideanColorDistance finder = new EuclideanColorDistance();

        // Act
        double actual = finder.distance(colorA, colorB);

        // Assert
        assertEquals(Math.sqrt(2 * 255 * 255), actual, 0.000001);
    }

    //Test small RGB differences
    @Test
    void distance_smallDifference_returnsFive() {
        // Arrange
        int colorA = 0x000000;
        int colorB = 0x030400;
        EuclideanColorDistance finder = new EuclideanColorDistance();
        // Act
        double actual = finder.distance(colorA, colorB);
        // Assert
        assertEquals(5.0, actual, 0.000001);
    }

    // Test distance is symmetric (tests whether reversing the two colors produces the same distance.)
    @Test
    void distance_reversedColors_returnsSameDistance() {
        // Arrange
        int colorA = 0x123456;
        int colorB = 0xABCDEF;
        EuclideanColorDistance finder = new EuclideanColorDistance();

        // Act
        double distanceAB = finder.distance(colorA, colorB);
        double distanceBA = finder.distance(colorB, colorA);

        // Assert
        assertEquals(distanceAB, distanceBA, 0.000001);
    }
    
    //Test Green channel only
    @Test
    void distance_blackAndGreen_returns255() {
        // Arrange
        int colorA = 0x000000;
        int colorB = 0x00FF00;
        EuclideanColorDistance finder = new EuclideanColorDistance();

        // Act
        double actual = finder.distance(colorA, colorB);

        // Assert
        assertEquals(255.0, actual, 0.000001);
    }
        
    // Blue channel only
    @Test
    void distance_blackAndBlue_returns255() {
        // Arrange
        int colorA = 0x000000;
        int colorB = 0x0000FF;
        EuclideanColorDistance finder = new EuclideanColorDistance();

        // Act
        double actual = finder.distance(colorA, colorB);

        // Assert
        assertEquals(255.0, actual, 0.000001);
    }
}
    

