package pad.ijvm;

import org.junit.Test;
import pad.ijvm.interfaces.IJVMInterface;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class Task3 {

	/**
	 * This is the example shown in the manual.
	 *
	 * Tests GOTO.
	 */
	@Test
	public void testGOTO1() throws IOException {
		IJVMInterface machine = MachineFactory.createIJVMInstance(new File("files/task3/GOTO1.ijvm"));

		machine.steps(1);
		assertEquals(0x31, machine.topOfStack());
		machine.steps(3);
		assertEquals(0x33, machine.topOfStack());
		machine.steps(1);
	}

	/**
	 * Tests a backward GOTO jump.
	 */
	@Test
	public void testGOTO2() throws IOException {
		IJVMInterface machine = MachineFactory.createIJVMInstance(new File("files/task3/GOTO2.ijvm"));

		machine.steps(1);
		assertEquals(0x31, machine.topOfStack());
		machine.steps(3);
		assertEquals(0x33, machine.topOfStack());
		machine.steps(3);
		assertEquals(0x32, machine.topOfStack());
		machine.steps(1);
	}


	/**
	 * Tests IFEQ
	 */
	@Test
	public void testIFEQ1() throws IOException {
		IJVMInterface machine = MachineFactory.createIJVMInstance(new File("files/task3/IFEQ1.ijvm"));

		int startPC;

		// L1
		machine.steps(1);
		assertEquals(0x05, machine.topOfStack());

		// L2 iteration 1
		machine.steps(6);
		assertEquals(0x04, machine.topOfStack());
		machine.steps(3);
		startPC = machine.getProgramCounter();

		// L2 iteration 2
		machine.steps(6);
		assertEquals(0x03, machine.topOfStack());
		machine.steps(3);
		assertEquals(startPC, machine.getProgramCounter());

		// L2 iteration 3
		machine.steps(6);
		assertEquals(0x02, machine.topOfStack());
		machine.steps(3);
		assertEquals(startPC, machine.getProgramCounter());

		// L2 iteration 4
		machine.steps(6);
		assertEquals(0x01, machine.topOfStack());
		machine.steps(3);
		assertEquals(startPC, machine.getProgramCounter());

		// L2 iteration 5
		machine.steps(6);
		assertEquals(0x00, machine.topOfStack());
		machine.steps(2);
		assertEquals("Should have branched to L3 now.", startPC + 15, machine.getProgramCounter());

	}


	/**
	 * Tests IFLT
	 */
	@Test
	public void testIFLT1() throws IOException {
		IJVMInterface machine = MachineFactory.createIJVMInstance(new File("files/task3/IFLT1.ijvm"));

		int oldPC = machine.getProgramCounter();

		// L1
		machine.steps(2);
		assertEquals("Should be at L2 now.", oldPC + 5, machine.getProgramCounter());
		oldPC = machine.getProgramCounter();

		// L2
		machine.steps(2);
		assertEquals("Should be at L3 now.", oldPC + 5, machine.getProgramCounter());
		oldPC = machine.getProgramCounter();

		// L3
		machine.steps(2);
		assertEquals("Should be at L4 now.", oldPC + 5, machine.getProgramCounter());
		oldPC = machine.getProgramCounter();

		// L4
		machine.steps(4);
		assertEquals("Should have jumped to L6 now.", oldPC + 19, machine.getProgramCounter());

		machine.steps(2);
		assertEquals("0x37 should have been pushed on the stack.", 0x37, machine.topOfStack());
	}


	/**
	 * Test IF_ICMPEQ
	 */
	@Test
	public void testIFICMEQ1() throws IOException {
		IJVMInterface machine = MachineFactory.createIJVMInstance(new File("files/task3/IFICMPEQ1.ijvm"));

		int oldPC = machine.getProgramCounter();

		// L1
		machine.steps(3);
		assertEquals("Should be at L2.", oldPC + 7, machine.getProgramCounter());
		oldPC = machine.getProgramCounter();

		// L2
		machine.steps(5);
		assertEquals("Should be at L3.", oldPC + 10, machine.getProgramCounter());
		oldPC = machine.getProgramCounter();

		// L3
		machine.steps(4);
		assertEquals("Should be at L4.", oldPC + 8, machine.getProgramCounter());
		oldPC = machine.getProgramCounter();

		// L4
		for (int i = 0; i < 3; i++) {
			machine.steps(3);
			assertEquals("Should be at L4.", oldPC, machine.getProgramCounter());
			oldPC = machine.getProgramCounter();
		}

		// L6
		machine.steps(2);
		assertEquals("Should be at L6.", oldPC + 9, machine.getProgramCounter());

		machine.steps(2);
		assertEquals("0x13 should have been pushed onto the stack.", 0x13, machine.topOfStack());

	}

}
