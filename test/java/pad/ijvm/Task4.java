package pad.ijvm;

import org.junit.Test;
import pad.ijvm.interfaces.IJVMInterface;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class Task4 {

    IJVMInterface machine;

    @Test
    public void testSimpleLdcw() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/task4/LoadTest1.ijvm"));
        machine.step();
        assertEquals("The first constant should be 1",machine.topOfStack(), 1);
        machine.step();
        assertEquals("The second constant should be 2", machine.topOfStack(), 2);
        machine.step();
        assertEquals("The third constant should be 3", machine.topOfStack(), 3);
    }

    @Test
    public void testSimpleLoad() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/task4/LoadTest3.ijvm"));
        machine.step();
        assertEquals("The top of the stack should be 42", machine.topOfStack(), 42);
        machine.step();
        assertEquals("The top of the stack should be 1", machine.topOfStack(), 1);
        machine.step();
        assertEquals("The top of the stack should be 42 again", machine.topOfStack(), 42);
        machine.step();
        assertEquals("The top of the stack should be 2", machine.topOfStack(), 2);
        machine.step();
        assertEquals("The top of the stack should be 42 again", machine.topOfStack(), 42);
        machine.step();
        assertEquals("The top of the stack should be 3", machine.topOfStack(), 3);
        machine.step();
        assertEquals("The top of the stack should be 42 again", machine.topOfStack(), 42);
        machine.step();
        assertEquals("The top of the stack should be 3", machine.topOfStack(), 3);
        machine.step();
        assertEquals("The top of the stack should be 2", machine.topOfStack(), 2);
        machine.step();
        assertEquals("The top of the stack should be 1", machine.topOfStack(), 1);
        machine.step();
        machine.step();
        machine.step();
        assertEquals("The top of the stack should be 42", machine.topOfStack(), 42);
    }

    @Test
    public void testComplexLoad() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/task4/LoadTest2.ijvm"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream p = new PrintStream(baos);
        machine.setOutput(p);
        machine.step();
        assertEquals("The top of the stack should be 42", machine.topOfStack(), 42);
        machine.step();
        assertEquals("The top of the stack should be 2", machine.topOfStack(), 2);
        machine.step();
        assertEquals("The top of the stack should be 2", machine.topOfStack(), 2);
        machine.step();
        assertEquals("The top of the stack should be 0", machine.topOfStack(), 0);
        machine.step();
        machine.step();
        assertEquals("The top of the stack should be 3", machine.topOfStack(), 3);
        machine.step();
        assertEquals("The top of the stack should be 42", machine.topOfStack(), 42);
        machine.step();
        assertEquals("The top of the stack should be 0", machine.topOfStack(), 0);
        machine.step();
        assertEquals("The top of the stack should be 3", machine.topOfStack(), 3);
        machine.step();
        assertEquals("The top of the stack should be 79", machine.topOfStack(), 79);
        machine.step();
        assertEquals("The top of the stack should be 3", machine.topOfStack(), 3);
        machine.step();
        assertEquals("The top of the stack should be 75", machine.topOfStack(), 75);
        machine.step();
        assertEquals("The top of the stack should be 3", machine.topOfStack(), 3);
    }

    @Test(timeout=2000)
    public void testIterationLoad() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/task4/LoadTest4.ijvm"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream p = new PrintStream(baos);
        machine.setOutput(p);
        machine.run();
        assertEquals("This test should output 'kjihgfedcbaabcd'", "kjihgfedcbaabcd", baos.toString());
    }

    @Test
    public void testIINC() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/task4/IINCTest.ijvm"));

        machine.steps(4);
        assertEquals("Variable 'hi' should have value 0 now.", 0, machine.getLocalVariable(0));
        assertEquals("Variable 'there' should have value 0 now.", 0, machine.getLocalVariable(1));

        machine.step();
        assertEquals("Variable 'hi' should have value 0 now.", 0, machine.getLocalVariable(0));
        machine.step();
        assertEquals("Variable 'hi' should have value 1 now.", 1, machine.getLocalVariable(0));
        machine.step();
        assertEquals("Variable 'hi' should have value 4 now.", 4, machine.getLocalVariable(0));

        machine.step();
        assertEquals("Variable 'there' should have value 0 now.", 0, machine.getLocalVariable(1));
        machine.step();
        assertEquals("Variable 'there' should have value -1 now.", -1, machine.getLocalVariable(1));
        machine.step();
        assertEquals("Variable 'there' should have value -4 now.", -4, machine.getLocalVariable(1));


    }



}
