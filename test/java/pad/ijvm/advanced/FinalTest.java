package pad.ijvm.advanced;


import org.junit.Test;
import pad.ijvm.MachineFactory;
import pad.ijvm.interfaces.IJVMInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class FinalTest {

    IJVMInterface machine;

    @Test(timeout=20000)
    public void tanenbaumTest() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/advanced/Tanenbaum.ijvm"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        machine.setOutput(ps);
        machine.run();
        assertEquals("Tanenbaum says NOT OK", "OK", baos.toString());
    }
}