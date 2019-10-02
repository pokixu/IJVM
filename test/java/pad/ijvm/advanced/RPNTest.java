package pad.ijvm.advanced;


import org.junit.Test;
import pad.ijvm.MachineFactory;
import pad.ijvm.interfaces.IJVMInterface;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class RPNTest {

    IJVMInterface machine;

    //All using the reverse polish program
    private String run(String input) throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/advanced/SimpleCalc.ijvm"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        PrintStream ps = new PrintStream(baos);

        machine.setOutput(ps);
        machine.setInput(bais);
        machine.run();
        return baos.toString();
    }

    @Test(timeout=5000)
    public void Test1() throws IOException {

        assertEquals("Test RPN 0 0 +", "0\n",run("0 0 +?."));
        assertEquals("Test RPN 0 9 +", "9\n",run("0 9 +?."));
        assertEquals("Test RPN 9 0 +", "9\n",run("9 0 +?."));
        assertEquals("Test RPN 9 9 -", "0\n",run("9 9 -?."));

    }

    @Test(timeout=5000)
    public void Test2() throws IOException {


        assertEquals("Test RPN 5 4 -", "1\n",run("    5 4 -?."));
        assertEquals("Test RPN 5 4 -", "8\n",run("  8  8 8  - + ?."));

    }

    @Test(timeout=5000)
    public void Test3() throws IOException {

        assertEquals("Test RPN long failed", "2\n",run("1 1 + 1 1 + 1 1 + 1 1 + 1 1 + +-++?."));
        assertEquals("Test RPN long failed", "1\n",run("9 8 -9 7-9 6-9 5-9 4-9 3-9 2-9 1-9 0- -+-+-+-+?."));
    }
}
