import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

public class BinarizingImageGroupFinderTest {

    // A pretend binarizer. It ignores the colors in the image and always
    // returns the grid we give it. It also remembers which image it was handed.
    private static class FakeBinarizer implements ImageBinarizer {
        int[][] gridToReturn;
        BufferedImage imageSeen;
        int timesCalled = 0;

        FakeBinarizer(int[][] gridToReturn) {
            this.gridToReturn = gridToReturn;
        }

        @Override
        public int[][] toBinaryArray(BufferedImage image) {
            imageSeen = image;
            timesCalled++;
            return gridToReturn;
        }

        @Override
        public BufferedImage toBufferedImage(int[][] image) {
            // Not used by BinarizingImageGroupFinder
            return null;
        }
    }

    // A pretend group finder. It doesn't search for anything and always
    // returns the groups we give it. It also remembers which grid it was handed.
    private static class FakeGroupFinder implements BinaryGroupFinder {
        List<Group> groupsToReturn;
        int[][] gridSeen;
        int timesCalled = 0;

        FakeGroupFinder(List<Group> groupsToReturn) {
            this.groupsToReturn = groupsToReturn;
        }

        @Override
        public List<Group> findConnectedGroups(int[][] image) {
            gridSeen = image;
            timesCalled++;
            return groupsToReturn;
        }
    }

    @Test
    void findConnectedGroups_returnsWhatGroupFinderReturns() {
        // Arrange
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        FakeBinarizer binarizer = new FakeBinarizer(new int[][] {
            {1, 0},
            {0, 0}
        });
        List<Group> fakeGroups = List.of(new Group(1, new Coordinate(0, 0)));
        FakeGroupFinder groupFinder = new FakeGroupFinder(fakeGroups);
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        // Act
        List<Group> groups = finder.findConnectedGroups(image);

        // Assert
        assertEquals(fakeGroups, groups);
    }

    // TODO tests to write:
    //
    // Handoffs (use the fakes' "seen" fields)
    // [x] the image passed in is the same one the binarizer receives
    //     (hint: assertSame(image, binarizer.imageSeen) checks it's the exact same object)
    @Test
    void findConnectedGroups_passesImageToBinarizer() {
    //Arrange
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        FakeBinarizer binarizer = new FakeBinarizer(new int[][] {
            {1, 0},
            {0, 0}
        });
        List<Group> fakeGroups = List.of(new Group(1, new Coordinate(0, 0)));
        FakeGroupFinder groupFinder = new FakeGroupFinder(fakeGroups);
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

    // Act
    finder.findConnectedGroups(image);
    //Assert
    assertSame(image, binarizer.imageSeen);

    }

    // [x] the grid the binarizer returns is the same one the group finder receives
    //     (assertSame(binarizer.gridToReturn, groupFinder.gridSeen))
    @Test
    void findConnectedGroups_passesGridToGroupFinder() {
    //Arrange
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        FakeBinarizer binarizer = new FakeBinarizer(new int[][] {
            {1, 0},
            {0, 0}
        });
        List<Group> fakeGroups = List.of(new Group(1, new Coordinate(0, 0)));
        FakeGroupFinder groupFinder = new FakeGroupFinder(fakeGroups);
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

    // Act
    finder.findConnectedGroups(image);
    //Assert
    assertSame(binarizer.gridToReturn, groupFinder.gridSeen);

    }
    // [ ] each fake is called exactly once (timesCalled == 1)
    @Test
    void findConnectedGroups_callsEachHelperOnce() {
    //Arrange
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        FakeBinarizer binarizer = new FakeBinarizer(new int[][] {
            {1, 0},
            {0, 0}
        });
        List<Group> fakeGroups = List.of(new Group(1, new Coordinate(0, 0)));
        FakeGroupFinder groupFinder = new FakeGroupFinder(fakeGroups);
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

    // Act
    finder.findConnectedGroups(image);
    //Assert
    assertEquals(1, binarizer.timesCalled);
    assertEquals(1, groupFinder.timesCalled);

    }
    //
    // Results
    // [ ] group finder returns an empty list -> result is an empty list
    
    // [ ] group finder returns several groups -> result has the same groups in the same order
    //
    // Real pieces together (no fakes)
    // [ ] use a real DistanceImageBinarizer (with a FakeDistanceFinder-style fake or the
    //     real EuclideanColorDistance once Wave 3 is done) and your real DfsBinaryGroupFinder.
    //     Make a small image with setRGB, work out the groups by hand, and check them.
}
