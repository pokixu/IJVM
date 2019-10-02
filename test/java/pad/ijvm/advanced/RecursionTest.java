package pad.ijvm.advanced;


import org.junit.Test;
import pad.ijvm.MachineFactory;
import pad.ijvm.interfaces.IJVMInterface;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class RecursionTest {

    IJVMInterface machine;
    //All using the reverse polish program
    private String run(String input) throws IOException
    {
        machine = MachineFactory.createIJVMInstance(new File("files/advanced/SimpleCalc.ijvm"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));


        PrintStream ps = new PrintStream(baos);
        machine.setOutput(ps);
        machine.setInput(bais);
        machine.run();
        return baos.toString();
    }


    @Test(timeout=1000)
    public void Test1() throws IOException
    {
        assertEquals("Tiny recursion test: ","2\n",run("2!?."));
    }

    @Test(timeout=10000)
    public void Test2() throws IOException
    {
        assertEquals("Smaller recursion test: ","5040\n",run("7!?."));
    }

    @Test(timeout=40000)
    public void Test3() throws IOException
    {
        assertEquals("Larger recursion test: ","40320\n",run("8!?."));
    }


    // Skip for now, since it takes way too long... Maybe performance contest??
    public void Test4() throws IOException
    {
        assertEquals("More complex test: ","24\n24\n24\n",run("99 5 + 4 / 7 1*- ! ? 99 5+4/4v1*-!&??."));
    }
}