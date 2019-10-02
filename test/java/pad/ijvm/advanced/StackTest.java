package pad.ijvm.advanced;

import org.junit.Test;
import pad.ijvm.MachineFactory;
import pad.ijvm.interfaces.IJVMInterface;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;


public class StackTest {
    IJVMInterface machine;

    @Test(timeout = 1000)
    public void test100() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/advanced/teststack.ijvm"));
        machine.step();
        // Push 100 elements to stack.
        machine.steps(2*100);
        assertEquals(0x2, machine.topOfStack());
    }

    @Test(timeout = 10000)
    public void test3000() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/advanced/teststack.ijvm"));
        machine.step();
        // Push 3000 elements to stack.
        machine.steps(2*3000);
        assertEquals(0x2, machine.topOfStack());
    }

    @Test(timeout = 20000)
    public void test50000() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/advanced/teststack.ijvm"));
        machine.step();
        // Push 50000 elements to stack.
        machine.steps(2*50000);
        assertEquals(0x2, machine.topOfStack());
    }

    @Test(timeout = 10000)
    public void testGetStackContents() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/advanced/teststack.ijvm"));
        machine.step();
        // Push 100 elements to stack.
        machine.steps(2*100);
        int[] stack = machine.getStackContents();
        int markers = 0;
        for (int i: stack) {
            if (i == 0x2) {
                markers++;
            }
        }
        assertTrue(stack.length >= 100);
        assertTrue(markers >= 99);

    }

    @Test(timeout = 10000)
    public void testMethodStack() throws IOException {
        machine = MachineFactory.createIJVMInstance(new File("files/advanced/teststack2.ijvm"));
        machine.steps(8);

        int[] stack1 = machine.getStackContents();
        int markers1 = 0;
        for (int i: stack1) {
            if (i == 0x2) {
                markers1++;
            }
        }
        assertEquals(4, markers1); // Check for 0x2

        // Perform invokevirtual
        machine.step();

        machine.steps(2*5000);
        int[] stack2 = machine.getStackContents();
        int markers2 = 0;
        for (int i: stack2) {
            if (i == 0x2) {
                markers2++;
            }
        }
        assertTrue(markers2 > 4000);

    }
}
