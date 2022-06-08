package gui;
import org.junit.Before;
import org.junit.Test;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileNotFoundException;

import static org.testng.Assert.fail;

public class interfaceTest {
    private Interface NewFrame;

    @Before
    public void SetUp() throws Exception {
        NewFrame = new Interface();
    }

    @Test
    public void TestPDFGenerator(){
        try {
            NewFrame.GeneratePDF("films_.xml", "films.pdf");
            fail();
        }catch(FileNotFoundException | Interface.XMLFileNotFoundExeption ex){
            System.out.println("catch");//pass
        }
    }

}
