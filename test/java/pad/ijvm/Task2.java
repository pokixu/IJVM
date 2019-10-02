package pad.ijvm;


import pad.ijvm.interfaces.IJVMInterface;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class Task2 {

    IJVMInterface machine;



    @Test
    public void testSimpleBipush() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/task2/TestBipush1.ijvm"));

        machine.step();
        assertEquals("The top of the stack should be 42.", 42, machine.topOfStack());
    }

    @Test
    public void testSignedBipush() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/task2/TestBipush2.ijvm"));

        machine.step();
        assertEquals("The top of the stack should be -42.", -42, machine.topOfStack());
    }

    @Test
    public void testSimpleIadd() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/task2/TestIadd1.ijvm"));

        machine.steps(3);
        assertEquals("The result of IADD should be 60.", 60, machine.topOfStack());
    }

    @Test
    public void testSignedIadd() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/task2/TestIadd2.ijvm"));

        machine.steps(3);
        assertEquals("The result of IADD should be -60.", -60, machine.topOfStack());
    }

    @Test
    public void testSimpleIsub() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/task2/TestIsub1.ijvm"));

        machine.steps(3);
        assertEquals("The result of ISUB should be -10.", -10, machine.topOfStack());
    }

    @Test
    public void testSignedIsub() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/task2/TestIsub2.ijvm"));

        machine.steps(3);
        assertEquals("The result of ISUB should be 10.", 10, machine.topOfStack());
    }

    @Test
    public void testSimpleIAND() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/task2/TestIAND1.ijvm"));

        machine.steps(5);
        assertEquals("The result of IAND should be 1.", 1, machine.topOfStack());
    }

    @Test
    public void testSimpleIOR() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/task2/TestIOR1.ijvm"));

        machine.steps(5);
        assertEquals("The result of IAND should be 127.", 127, machine.topOfStack());
    }

    @Test
    public void testSwap() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/task2/TestSwap1.ijvm"));

        machine.step();
        assertEquals("The TOS should be 10.", 10, machine.topOfStack());
        machine.step();
        assertEquals("The TOS should be 20.", 20, machine.topOfStack());
        machine.step();
        assertEquals("Swap should swap the variables.", 10, machine.topOfStack());
        machine.step();
        assertEquals("The top if the stack should now be 20.", 20, machine.topOfStack());
    }

    @Test
    public void testSimpleStackOperations() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/task2/TestPop1.ijvm"));

        machine.steps(3);
        assertEquals("Pop should pop the value of the stack.", 10, machine.topOfStack());
        machine.steps(3);
        assertEquals("The result of IADD should be 50.", 50, machine.topOfStack());
        machine.step();
        assertEquals("Pop should pop the value of the stack.", 10, machine.topOfStack());
    }


}
