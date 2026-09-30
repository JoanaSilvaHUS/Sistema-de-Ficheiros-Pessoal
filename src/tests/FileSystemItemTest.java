import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.pa.adts.model.FileSystemItem;


import static org.junit.jupiter.api.Assertions.*;

class FileSystemItemTest {

   FileSystemItem fileSystemItem;

    @BeforeEach
    void Setup(){
        fileSystemItem = new FileSystemItem("My File System Item",false);
    }

    @Test
    void Test_ifName_isCorrect(){
        assertEquals("My File System Item", fileSystemItem.getName());
        fileSystemItem.setName("Item System File My");
        assertEquals("Item System File My", fileSystemItem.getName());
    }

    @Test
    void Test_if_ChangeCount_is_Correct(){
        assertEquals(0,fileSystemItem.getChangeCount());
        fileSystemItem.setChangeCount();
        assertEquals(1,fileSystemItem.getChangeCount());
    }

    @Test
    void Test_isDirectory(){
        assertFalse(fileSystemItem.isDirectory());
        FileSystemItem fileSystemItem2 = new FileSystemItem("My File System Item 2",true);
        assertTrue(fileSystemItem2.isDirectory());
    }

    @Test
    void Test_isHidden(){
        assertFalse(fileSystemItem.isHidden());
        fileSystemItem.setHidden(true);
        assertTrue(fileSystemItem.isHidden());
    }

    @Test
    void Test_isLocked(){
        assertFalse(fileSystemItem.isLocked());
    }

}