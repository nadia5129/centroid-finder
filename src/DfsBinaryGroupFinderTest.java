import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class DfsBinaryGroupFinderTest {

    @Test
    void findConnectedGroups_singlePixel_returnsOneGroupOfSizeOne() {
        // Arrange: a 3x3 image with one white pixel at row 1, column 2
        int[][] image = {
            {0, 0, 0},
            {0, 0, 1},
            {0, 0, 0}
        };
        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();

        // Act
        List<Group> groups = finder.findConnectedGroups(image);

        // Assert: row 1, column 2 means x = 2, y = 1
        List<Group> expected = List.of(new Group(1, new Coordinate(2, 1)));
        assertEquals(expected, groups);
    }
}
