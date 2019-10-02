package pad.ijvm.advanced;

import org.junit.Test;
import pad.ijvm.MachineFactory;
import pad.ijvm.interfaces.IJVMInterface;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class    AdvancedTestWide {

    IJVMInterface machine;

    @Test(timeout=1000)
    public void testWide1() throws IOException {

        machine = MachineFactory.createIJVMInstance(new File("files/advanced/test-wide1.ijvm"));
        machine.step();
        machine.step();
        machine.step();
        machine.step();

        machine.step();
        machine.step();
        assertEquals("Local variable 1 should be 0x1", 0x1, machine.getLocalVariable(1));

        machine.step();
        machine.step();
        assertEquals("Local variable 257 should be 0x2", 0x2, machine.getLocalVariable(257));


        machine.step();
        assertEquals("ILOAD should load variable 1 as 0x1", 0x1, machine.topOfStack());
        machine.step();
        assertEquals("ILOAD should load variable 257 as 0x2", 0x2, machine.topOfStack());


    }


    @Test(timeout=1000)
    public void testWide2() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/advanced/test-wide2.ijvm"));
        machine.step();
        machine.step();
        machine.step();
        machine.step();

        machine.step();
        machine.step();
        assertEquals("Local variable 1 should be 0x1", 0x1, machine.getLocalVariable(1));
        machine.step();
        machine.step();
        assertEquals("Local variable 32768 should be 0x2", 0x2, machine.getLocalVariable(32768));

        machine.step();
        assertEquals("ILOAD should load variable 1 as 0x1", 0x1, machine.topOfStack());
        machine.step();
        assertEquals("ILOAD should load variable 32768 as 0x2", 0x2, machine.topOfStack());
    }
}