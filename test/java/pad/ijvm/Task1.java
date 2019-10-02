package pad.ijvm;

import org.junit.Test;
import pad.ijvm.interfaces.IJVMInterface;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

public class Task1 {

    @Test
    public void testMachineFactory() throws IOException {
        IJVMInterface machine = MachineFactory.createIJVMInstance(new File("files/task1/program2.ijvm"));
        assertNotNull("MachineFactory not implemented, returned null.", machine);
    }

    @Test
    public void testProgram1() throws IOException {
        IJVMInterface machine = MachineFactory.createIJVMInstance(new File("files/task1/program1.ijvm"));
        byte[] text = machine.getText();

        assertEquals("Length of 'method' block should be equal to 6.",  6, text.length );
        assertEquals("First instruction should be equal to 'BIPUSH'.", (byte) 0x10, text[0]);
        assertEquals("Second instruction should be equal to 'BIPUSH'.", (byte) 0x10, text[2]);
        assertEquals("Third instruction should be equal to 'IADD'.", (byte) 0x60, text[4]);
        assertEquals("Fourth instruction should be equal to 'OUT'.", (byte) 0xFD, text[5]);
    }

    @Test
    public void testProgram2() throws IOException {
        IJVMInterface machine = MachineFactory.createIJVMInstance(new File("files/task1/program2.ijvm"));
        byte[] text = machine.getText();

        assertEquals("Length of 'method' block should be equal to 15.", 15, text.length);
        assertEquals("First instruction should be equal to 'NOP'.", (byte) 0x00, text[0]);
        assertEquals("Second instruction should be equal to 'LDC_W'.", (byte) 0x13, text[1]);
        assertEquals("Third instruction should be equal to 'DUP'.", (byte) 0x59, text[4]);
        assertEquals("Fourth instruction should be equal to 'LDC_W'.", (byte) 0x13, text[5]);
        assertEquals("Fifth instruction should be equal to 'IADD'.", (byte) 0x60, text[8]);
        assertEquals("Sixth instruction should be equal to 'LDC_W'.", (byte) 0x13, text[9]);
        assertEquals("Seventh instruction should be equal to 'IADD'.", (byte) 0x60, text[12]);
        assertEquals("Eight instruction should be equal to 'OUT'.", (byte) 0xFD, text[13]);
        assertEquals("Ninth instruction should be equal to 'NOP'.", (byte) 0x00, text[14]);

    }

    @Test
    public void testProgramCounter() throws IOException {
        IJVMInterface machine = MachineFactory.createIJVMInstance(new File("files/task1/program1.ijvm"));

        assertEquals("Program counter should be initialized to 0", 0, machine.getProgramCounter());
    }
}
