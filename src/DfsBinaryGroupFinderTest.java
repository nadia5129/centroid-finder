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

    // TODO tests to write (x = column, y = row):
    //
    // Basic cases
    // [ ] all zeros -> empty list
    // [ ] all ones -> one group covering the whole image, centroid in the middle
    // [ ] single pixel at (0, 0) -> top-left corner works
    // [ ] single pixel at the bottom-right corner -> edges don't go out of bounds
    // [ ] 1x1 image containing a 1
    // [ ] 1x1 image containing a 0
    //
    // Connectivity
    // [ ] two pixels side by side (horizontal) -> one group of size 2
    // [ ] two pixels stacked (vertical) -> one group of size 2
    // [ ] two pixels touching only diagonally -> two separate groups of size 1
    // [ ] L-shape -> one group (DFS follows the turn)
    // [ ] U-shape or snake -> one group (DFS goes back on itself)
    // [ ] ring with a hole of 0s in the middle -> one group
    // [ ] two groups separated by a column of 0s
    //
    // Centroid math
    // [ ] centroid needs integer division (e.g. pixels at x=0 and x=1 -> x=0)
    // [ ] non-symmetric shape where you calculate the centroid by hand
    //
    // Image shapes
    // [ ] wide image (1 row, many columns)
    // [ ] tall image (many rows, 1 column)
    // [ ] non-square image (e.g. 3x5) -> x and y aren't swapped
    //
    // Sorting (descending: size, then x, then y)
    // [ ] groups of different sizes -> biggest first
    // [ ] same size, different x -> bigger x first
    // [ ] same size, same x, different y -> bigger y first
    //
    // Errors
    // [ ] null image -> NullPointerException
    // [ ] one null row -> NullPointerException
    // [ ] empty array (new int[0][0]) -> IllegalArgumentException
    // [ ] rows of different lengths -> IllegalArgumentException
    // [ ] a value that isn't 0 or 1 (like 2 or -1) -> IllegalArgumentException
}
