package pad.ijvm.advanced;

import org.junit.Test;
import pad.ijvm.MachineFactory;
import pad.ijvm.interfaces.IJVMInterface;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class AdvancedTestNestedInvoke {

    IJVMInterface machine;

    @Test(timeout=5000)
    public void testNestedInvokeSimple() throws IOException {

        machine = MachineFactory.createIJVMInstance(new File("files/advanced/test-nestedinvoke-simple.ijvm"));
        // in main
        machine.step(); // LDC_W
        int mainPC =  machine.getProgramCounter() + 3;
        machine.step(); // INVOKEVIRTUAL magic

        // in magic
        assertNotEquals("magic: Program counter should have changed", mainPC, machine.getProgramCounter());
        machine.step(); // LDC_W
        assertNotEquals("LDC_W: 0x9 should not be on the stack", 0x9, machine.topOfStack());
        int magicPC =  machine.getProgramCounter() + 3;
        machine.step(); // INVOKEVIRTUAL addone

        // in addone
        assertNotEquals("addone: Program counter should have changed after INVOKEVIRTUAL", magicPC, machine.getProgramCounter());
        int addonePC =  machine.getProgramCounter() + 3;
        machine.step(); // BIPUSH 0
        machine.step(); // IRETURN
        assertNotEquals("magic: Program counter should have changed after IRETURN", addonePC, machine.getProgramCounter());
        assertEquals("magic: Program counter should have returned to after INVOKEVIRTUAL", magicPC, machine.getProgramCounter());
        machine.step(); // IRETURN
        assertNotEquals("main: Program counter should have changed after IRETURN", magicPC, machine.getProgramCounter());
        assertEquals("main: Program counter should have returned to after INVOKEVIRTUAL", mainPC, machine.getProgramCounter());

    }

    @Test(timeout=5000)
    public void testNestedInvoke() throws IOException {

        machine = MachineFactory.createIJVMInstance(new File("files/advanced/test-nestedinvoke.ijvm"));
        // in main
        machine.step(); // LDC_W
        machine.step(); // BIPUSH 1
        machine.step(); // LDC_W 5
        machine.step(); // INVOKEVIRTUAL magic

        // in magic
        machine.step(); // LDC_W 15
        machine.step(); // ILOAD 1
        machine.step(); // ILOAD 5
        machine.step(); // IADD
        assertEquals("IADD: 0x6 should be on the stack", 0x6, machine.topOfStack());
        machine.step(); // ISUB
        assertEquals("ISUB: 0x9 should be on the stack", 0x9, machine.topOfStack());
        machine.step(); // DUP
        assertEquals("DUP: 0x9 should be on the stack", 0x9, machine.topOfStack());
        machine.step(); // LDC_W
        assertNotEquals("LDC_W: 0x9 should not be on the stack", 0x9, machine.topOfStack());
        machine.step(); // SWAP
        assertEquals("SWAP: 0x9 should be on the stack", 0x9, machine.topOfStack());
        machine.step(); // INVOKEVIRTUAL addone

        // in addone
        machine.step(); // ILOAD var
        assertEquals("LDC_W var: 0x9 should be on the stack", 0x9, machine.topOfStack());
        machine.step(); // BIPUSH 1
        assertEquals("BIPUSH 1: 0x1 should be on the stack", 0x1, machine.topOfStack());
        machine.step(); // IADD
        assertEquals("0xA should be on the stack", 0xA, machine.topOfStack());
        machine.step(); // IRETURN
        assertEquals("0xA should be on the stack", 0xA, machine.topOfStack());
        machine.step(); // DUP
        machine.step(); // IRETURN

        machine.step(); // NOP
        assertEquals("TOS should now be 0xA", 0xA, machine.topOfStack());

    }

}
