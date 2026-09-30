import org.junit.Test;
import pt.pa.adts.model.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TreeLinkedTest {

    private TreeLinked<String> treeLinked = new TreeLinked<>();

   @Test
   public void testSize() {
       Position<String> rootPosition = treeLinked.insert(null, "root");
       treeLinked.insert(rootPosition, "elem1");

       Position<String> parent = treeLinked.root();

       assertEquals(2, treeLinked.size());
   }

    @Test
    public void testIsEmpty() {
       assertTrue(treeLinked.isEmpty());
    }

    @Test
    public void testReplace() {
        Position<String> position1 = treeLinked.insert(null, "root");
        Position<String> position2 = treeLinked.insert(position1, "child1");

        String replacedElement = treeLinked.replace(position2, "newChild");
        assertEquals("child1", replacedElement);
        assertEquals("newChild", position2.element());

        assertThrows(InvalidPositionException.class, () -> treeLinked.replace(null, "newRoot"));
    }

    @Test
    public void testRoot() {
        assertThrows(EmptyTreeException.class, () -> treeLinked.root());

        Position<String> rootPosition = treeLinked.insert(null, "root");
        treeLinked.insert(rootPosition, "child1");
        treeLinked.insert(rootPosition, "child2");
        assertEquals(rootPosition, treeLinked.root());
    }

    @Test
    public void testParent() {
        Position<String> rootPosition = treeLinked.insert(null, "root");
        Position<String> child1 = treeLinked.insert(rootPosition, "child1");
        Position<String> child2 = treeLinked.insert(rootPosition, "child2");

        assertEquals(rootPosition, treeLinked.parent(child1));
        assertEquals(rootPosition, treeLinked.parent(child2));
    }

    @Test
    public void testParentWithInvalidPositionException() {
        assertThrows(InvalidPositionException.class, () -> treeLinked.parent(null));
    }

    @Test
    public void testParentWithBoundaryViolationException() {
        assertThrows(BoundaryViolationException.class, () -> {
            Position<String> rootPosition = treeLinked.insert(null, "root");
            treeLinked.parent(rootPosition);
        });
    }

    @Test
    public void testChildren() {
        Position<String> rootPosition = treeLinked.insert(null, "root");
        Position<String> child1 = treeLinked.insert(rootPosition, "child1");
        Position<String> child2 = treeLinked.insert(rootPosition, "child2");

        Iterable<Position<String>> children = treeLinked.children(rootPosition);

        List<Position<String>> expectedChildren = Arrays.asList(child1, child2);

        assertIterableEquals(expectedChildren, children);

        Position<String> leafNode = treeLinked.insert(child1, "leaf");
        children = treeLinked.children(leafNode);

        assertFalse(children.iterator().hasNext());

        assertThrows(InvalidPositionException.class, () -> treeLinked.children(null));
    }

    @Test
    public void testIsInternal() {
        Position<String> rootPosition = treeLinked.insert(null, "root");

        assertFalse(treeLinked.isInternal(rootPosition));
        Position<String> childPosition = treeLinked.insert(rootPosition, "child");

        assertTrue(treeLinked.isInternal(rootPosition));
        assertFalse(treeLinked.isInternal(childPosition));
    }

    @Test
    public void testIsExternal() {
        Position<String> rootPosition = treeLinked.insert(null, "root");
        Position<String> child1 = treeLinked.insert(rootPosition, "child1");

        assertFalse(treeLinked.isExternal(rootPosition));
        assertTrue(treeLinked.isExternal(child1));

        assertThrows(InvalidPositionException.class, () -> treeLinked.isExternal(null));
    }

    @Test
    public void testIsRoot() {
        Position<String> rootPosition = treeLinked.insert(null, "root");
        assertTrue(treeLinked.isRoot(rootPosition));

        Position<String> childPosition = treeLinked.insert(rootPosition, "child");

        assertFalse(treeLinked.isRoot(childPosition));

        assertThrows(InvalidPositionException.class, () -> treeLinked.isRoot(null));
    }

    @Test
    public void testInsert() {
        Position<String> rootPosition = treeLinked.insert(null, "root");
        assertEquals("root", rootPosition.element());
        assertTrue(treeLinked.isRoot(rootPosition));

        Position<String> childPosition = treeLinked.insert(rootPosition, "child");
        assertEquals("child", childPosition.element());
        assertTrue(treeLinked.isInternal(rootPosition));
        assertTrue(treeLinked.isExternal(childPosition));
    }

    @Test
    public void testInsertWithOrder() {
        Position<String> rootPosition = treeLinked.insert(null, "root");
        Position<String> child1 = treeLinked.insert(rootPosition, "child1");
        Position<String> child2 = treeLinked.insert(rootPosition, "child2");

        Position<String> child3 = treeLinked.insert(rootPosition, "child3", 1);
        Iterable<Position<String>> children = treeLinked.children(rootPosition);
        List<Position<String>> expectedOrder = Arrays.asList(child1, child3, child2);

        assertIterableEquals(expectedOrder, children);
        Iterable<String> elements = treeLinked.elements();
        List<String> expectedElements = Arrays.asList("root", "child1", "child3", "child2");

        assertIterableEquals(expectedElements, elements);
        assertThrows(BoundaryViolationException.class, () -> treeLinked.insert(rootPosition, "child4", 5));
    }

    @Test
    public void testRemove() {
        Position<String> rootPosition = treeLinked.insert(null, "root");
        Position<String> child1 = treeLinked.insert(rootPosition, "child1");

        String removedElement = treeLinked.remove(child1);
        assertEquals("child1", removedElement);
        assertFalse(treeLinked.isInternal(child1));
    }

    @Test
    public void testPositions() {
        Position<String> rootPosition = treeLinked.insert(null, "root");
        Position<String> child1 = treeLinked.insert(rootPosition, "child1");
        Position<String> child2 = treeLinked.insert(rootPosition, "child2");

        Iterable<Position<String>> positions = treeLinked.positions();

        List<Position<String>> expectedPositions = Arrays.asList(rootPosition, child1, child2);
        for (Position<String> expected : expectedPositions) {
            boolean isPositionPresent = false;
            for (Position<String> actual : positions) {
                if (actual == expected) {
                    isPositionPresent = true;
                    break;
                }
            }
            assertTrue(isPositionPresent);
        }
    }

    @Test
    public void testElements() {
        Position<String> rootPosition = treeLinked.insert(null, "root");
        Position<String> child1 = treeLinked.insert(rootPosition, "child1");
        Position<String> child2 = treeLinked.insert(rootPosition, "child2");

        Iterable<String> elements = treeLinked.elements();
        List<String> expectedElements = Arrays.asList("root", "child1", "child2");

        for (String expected : expectedElements) {
            boolean isElementPresent = false;
            for (String actual : elements) {
                if (actual.equals(expected)) {
                    isElementPresent = true;
                    break;
                }
            }
            assertTrue(isElementPresent);
        }
    }

    @Test
    public void testHeight() {
        assertEquals(-1, treeLinked.height());

        Position<String> rootPosition = treeLinked.insert(null, "root");
        assertEquals(0, treeLinked.height());

        Position<String> child1 = treeLinked.insert(rootPosition, "child1");
        Position<String> child2 = treeLinked.insert(rootPosition, "child2");

        assertEquals(1, treeLinked.height());

        Position<String> leafNode = treeLinked.insert(child1, "leaf");
        assertEquals(2, treeLinked.height());
    }

    @Test
    public void testMove() {
        TreeLinked<String> tree = new TreeLinked<>();
        Position<String> root = tree.insert(null, "root");
        Position<String> child1 = tree.insert(root, "child1");
        Position<String> child2 = tree.insert(root, "child2");

        tree.move(child1, child2);

        assertTrue(tree.isInternal(child2));
        assertFalse(tree.isInternal(child1));

        Iterable<Position<String>> child2Children = tree.children(child2);
        boolean containsChild1 = false;
        for (Position<String> child : child2Children) {
            if (child.element().equals("child1")) {
                containsChild1 = true;
                break;
            }
        }
        assertTrue(containsChild1);
    }

    @Test
    public void testIsAncestor() {
        Position<String> root = treeLinked.insert(null, "root");
        Position<String> child = treeLinked.insert(root, "child");

        assertTrue(treeLinked.isAncestor(child, root));
        assertFalse(treeLinked.isAncestor(root, child));
    }

    @Test
    public void testDegree() {
        Position<String> rootPosition = treeLinked.insert(null, "root");
        treeLinked.insert(rootPosition, "child1");
        treeLinked.insert(rootPosition, "child2");
        treeLinked.insert(rootPosition, "child3");

        assertEquals(3, treeLinked.degree(rootPosition));

        Position<String> leafNode = treeLinked.children(rootPosition).iterator().next();
        assertEquals(0, treeLinked.degree(leafNode));

        assertThrows(InvalidPositionException.class, () -> treeLinked.degree(null));
    }
}