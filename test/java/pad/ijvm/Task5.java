package pad.ijvm;

import org.junit.Test;
import pad.ijvm.interfaces.IJVMInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class Task5 {

    IJVMInterface machine;

    @Test
    public void testInvokeNoArgs() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/task5/TestInvokeNoArgs.ijvm"));
        machine.step();
        machine.step();
        assertEquals("The TOS should be 0x42",machine.topOfStack(), 0x42);
        machine.step();
        machine.step();
        assertEquals("The TOS in the new method should be 0x43",machine.topOfStack(), 0x43);
        machine.step();
        machine.step();
        assertEquals("After IRETURN the TOS should be 0x43",machine.topOfStack(), 0x43);
    }

    @Test(timeout=5000)
    public void testInvoke1() throws IOException {

        machine = MachineFactory.createIJVMInstance(new File("files/task5/test-invokevirtual1.ijvm"));
        machine.step();
        machine.step();
        machine.step();


        machine.step();
        assertNotEquals("Program counter should have changed after INVOKEVIRTUAL", 4, machine.getProgramCounter());

        machine.step();
        assertEquals("0x1 should have been pushed to the stack", 0x1, machine.topOfStack());


        machine.step();
        //assertEquals("Method should have returned with value 0x1", 0x1, machine.topOfStack());
        machine.step();
        //assertEquals("TOS should now be 0x2", 0x2, machine.topOfStack());

    }

    @Test(timeout=5000)
    public void testIreturn1() throws IOException {

        machine = MachineFactory.createIJVMInstance(new File("files/task5/test-invokevirtual1.ijvm"));
        machine.step();
        machine.step();
        machine.step();


        machine.step();
        //assertNotEquals("Program counter should have changed after INVOKEVIRTUAL", 4, machine.getProgramCounter());

        machine.step();
        //assertEquals("0x1 should have been pushed to the stack", 0x1, machine.topOfStack());


        machine.step();
        assertEquals("Method should have returned with value 0x1", 0x1, machine.topOfStack());
        machine.step();
        assertEquals("TOS should now be 0x2", 0x2, machine.topOfStack());

    }

    @Test(timeout=5000)
    public void testInvoke2() throws IOException {

        machine = MachineFactory.createIJVMInstance(new File("files/task5/test-invokevirtual2.ijvm"));
        machine.step();
        machine.step();
        machine.step();
        machine.step();
        machine.step();


        machine.step();
        assertNotEquals("Program counter should have changed after INVOKEVIRTUAL", 6, machine.getProgramCounter());

        machine.step();
        assertEquals("Local variable a should have been loaded", 0x2, machine.topOfStack());
        machine.step();
        assertEquals("Local variable b should have been loaded", 0x3, machine.topOfStack());


        machine.step();
        machine.step();
        //assertEquals("Method should have returned with value 0x5", 0x5, machine.topOfStack());
        machine.step();
        //assertEquals("TOS should now be 0x2", 0x2, machine.topOfStack());

    }

    @Test(timeout=5000)
    public void testIreturn2() throws IOException {

        machine = MachineFactory.createIJVMInstance(new File("files/task5/test-invokevirtual2.ijvm"));
        machine.step();
        machine.step();
        machine.step();
        machine.step();
        machine.step();


        machine.step();
        //assertNotEquals("Program counter should have changed after INVOKEVIRTUAL", 6, machine.getProgramCounter());

        machine.step();
        //assertEquals("Local variable a should have been loaded", 0x2, machine.topOfStack());
        machine.step();
        //assertEquals("Local variable b should have been loaded", 0x3, machine.topOfStack());


        machine.step();
        machine.step();
        assertEquals("Method should have returned with value 0x5", 0x5, machine.topOfStack());
        machine.step();
        assertEquals("TOS should now be 0x2", 0x2, machine.topOfStack());

    }

}
