import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.pa.adts.model.File;


import static org.junit.jupiter.api.Assertions.*;


class FileTest {

    File file;

    @BeforeEach
    void SetUp(){
        file = new File("myFile",false,false,"Apple, Orange, Banana");
    }

    @Test
    void Test_if_getSize_IsCorrect(){
        assertEquals(file.getSizeInBytesForFile(), file.getSizeInBytes());
    }

    @Test
    void Test_if_SetSize_IsWorking(){
        assertEquals(file.getSizeInBytesForFile(), file.getSizeInBytes());
        file.setSizeInBytes(file.getSizeInBytesForFile());
        assertEquals(file.getSizeInBytesForFile(), file.getSizeInBytes());
    }

    @Test
    void Test_if_Conteudo_IsRight(){
        assertEquals("Apple, Orange, Banana", file.getContent());
    }

    @Test
    void Test_if_SetConteudo_IsWorking(){
        assertEquals("Apple, Orange, Banana", file.getContent());
        file.setContent("Dog, Cat, Lion");
        assertEquals("Dog, Cat, Lion", file.getContent());
    }

}