package pad.ijvm.advanced;


import org.junit.Test;

import pad.ijvm.MachineFactory;
import pad.ijvm.interfaces.IJVMInterface;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Unit test for Diamond.jas
 * Not super-exhaustive but should do
 *
 * @author gkarlos
 *
 */

public class DiamondTest {

    IJVMInterface machine;
    String input;
    ByteArrayOutputStream baos;
    ByteArrayInputStream bais;
    PrintStream ps;



    private void newMachine(String input) {
        try {
            machine = MachineFactory.createIJVMInstance(new File("./files/advanced/Diamond.ijvm"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        baos = new ByteArrayOutputStream();
        bais = new ByteArrayInputStream(input.getBytes());
        ps = new PrintStream(baos);
        machine.setOutput(ps);
        machine.setInput(bais);
    }

    private long countChar(String s, char c) { return s.chars().filter(chr -> chr == c).count(); }

    private String hex(int op) { return Integer.toHexString(op); }

    @Test(timeout=20000)
    public void testIn() throws IOException {

        newMachine("5");

        machine.step();
        machine.step();

        assertEquals("Top of stack should be \"5\"", 0x35, machine.topOfStack());

        newMachine("! #!( !) #! 5");

        machine.step();
        machine.step();
        assertEquals("Top of stack should be \"!\"", 0x21, machine.topOfStack());

        machine.steps(8 * 5);
        assertEquals("Top of stack should be \" \"", 0x20, machine.topOfStack());

        machine.steps(20);
        assertEquals("Top of stack should be \"5\"", 0x35, machine.topOfStack());

    }

    @Test(timeout=20000)
    public void testOut() throws IOException {
        newMachine("5");
        machine.run();
        assertEquals("Output should have 9 lines", 9, countChar(baos.toString(), '\n'));

        newMachine("4");
        machine.run();
        String[] out = baos.toString().split("\n");
        assertEquals("First line should contain 1 4",   1, countChar(out[0], '4'));
        assertEquals("Second line should contain 3 3s", 3, countChar(out[1], '3'));
        assertEquals("Third line should contain 5 2s",  5, countChar(out[2], '2'));
        assertEquals("Fourth line should contain 7 1s", 7, countChar(out[3], '1'));
        assertEquals("Fifth line should contain 5 2s",  5, countChar(out[4], '2'));
        assertEquals("Sixth line should contain 3 3s",  3, countChar(out[5], '3'));
        assertEquals("Seventh line should contain 1 4", 1, countChar(out[6], '4'));

        assertEquals("First line should contain 4 spaces",  4, countChar(out[0], ' '));
        assertEquals("Second line should contain 3 spaces", 3, countChar(out[1], ' '));
        assertEquals("Third line should contain 2 spaces",  2, countChar(out[2], ' '));
        assertEquals("Fourth line should contain 1 space",  1, countChar(out[3], ' '));
        assertEquals("Fifth line should contain 2 spaces",  2, countChar(out[4], ' '));
        assertEquals("Sixth line should contain 3 spaces",  3, countChar(out[5], ' '));
        assertEquals("Seventh line should contain 4 spaces",4, countChar(out[6], ' '));

        newMachine("! (!) #4");
        machine.run();
        out = baos.toString().split("\n");
        assertEquals("First line should contain 1 4",   1, countChar(out[0], '4'));
        assertEquals("Second line should contain 3 3s", 3, countChar(out[1], '3'));
        assertEquals("Third line should contain 5 2s",  5, countChar(out[2], '2'));
        assertEquals("Fourth line should contain 7 1s", 7, countChar(out[3], '1'));
        assertEquals("Fifth line should contain 5 2s",  5, countChar(out[4], '2'));
        assertEquals("Sixth line should contain 3 3s",  3, countChar(out[5], '3'));
        assertEquals("Seventh line should contain 1 4", 1, countChar(out[6], '4'));

        assertEquals("First line should contain 4 spaces",  4, countChar(out[0], ' '));
        assertEquals("Second line should contain 3 spaces", 3, countChar(out[1], ' '));
        assertEquals("Third line should contain 2 spaces",  2, countChar(out[2], ' '));
        assertEquals("Fourth line should contain 1 space",  1, countChar(out[3], ' '));
        assertEquals("Fifth line should contain 2 spaces",  2, countChar(out[4], ' '));
        assertEquals("Sixth line should contain 3 spaces",  3, countChar(out[5], ' '));
        assertEquals("Seventh line should contain 4 spaces",4, countChar(out[6], ' '));

        newMachine("0");
        machine.run();
        for( String s: baos.toString().split("\n") )
            assertEquals("Nothing should be printed", 0, s.length());

        newMachine("!#&&0");
        machine.run();
        for( String s: baos.toString().split("\n") )
            assertEquals("Nothing should be printed", 0, s.length());


    }

    static final byte NOP = 0x00;
    static final byte BIPUSH = 0x10;
    static final byte DUP = 0x59;
    static final byte POP = 0x57;
    static final byte SWAP = 0x5F;
    static final byte IADD = 0x60;
    static final byte ISUB = 0x64;
    static final byte IAND = 0x7E;
    static final byte IOR = (byte) 0xb0;
    static final byte GOTO = (byte) 0xA7;
    static final byte IFEQ = (byte) 0x99;
    static final byte IFLT = (byte) 0x9b;
    static final byte IF_ICMPEQ = (byte) 0x9f;
    static final byte LDC_W = 0x13;
    static final byte ISTORE = 0x36;
    static final byte ILOAD = 0x15;
    static final byte IINC = (byte) 0x84;
    static final byte WIDE = (byte) 0xC4;
    static final byte INVOKEVIRTUAL = (byte) 0xB6;
    static final byte IRETURN = (byte) 0xAC;
    static final byte IN = (byte) 0xfc;
    static final byte OUT = (byte) 0xfd;
    static final byte HALT = (byte) 0xFF;


    @Test(timeout=20000)
    public void testRecursiveCalls() throws Exception {
        newMachine("3");
        machine.steps(7); // first diamond


        machine.steps(6); //ifeq
        assertEquals("Next instruction must be LDC_W", LDC_W, machine.getInstruction());
        machine.steps(4); //print1 call
        machine.steps(5);
        assertEquals("Next instruction must be ILOAD", ILOAD, machine.getInstruction());


        int var1, var2;
        machine.steps(4);
        assertEquals("Top of stack should be 0xDEAD", 0xDEAD, machine.topOfStack());
        machine.step();
        var1 = machine.getLocalVariable(1);
        assertEquals("Top of stack should be var 1", machine.getLocalVariable(1), machine.topOfStack());
        machine.step();
        var2 = machine.getLocalVariable(2);
        assertEquals("Top of stack should be var 2", machine.getLocalVariable(2), machine.topOfStack());
        machine.step();


        assertEquals("var 1 should equal what was previously pushed ", var1, machine.getLocalVariable(1));
        assertEquals("var 2 should equal what was previously pushed ", var2, machine.getLocalVariable(2));


        machine.steps(11);
        machine.steps(12);

        machine.steps(7);  //return
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.steps(2);
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.steps(2);
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.steps(2);
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.step();

        // Preparing for 2nd print
        assertEquals("Expected LDC_W", LDC_W, machine.getInstruction());
        assertEquals("Top of stack should be 0xDEAD", 0xDEAD, machine.getConstant(1));

        machine.steps(3);

        int invokes = 0;
        int returns = 0;

        for(int i = 0; i < 22; i++){
            if(machine.getInstruction() == INVOKEVIRTUAL)
                invokes++;
            if(machine.getInstruction() == IRETURN)
                returns++;

            machine.step();
        }

        assertEquals("Expected exactly 2 calls to INVOKEVIRTUAL", 2, invokes);

        assertEquals("Expected exactly 2 calls to IRETURN", 2, returns);



        //Printing new line here
        machine.step();
        assertEquals("Top of stack should be \"\\n\"", '\n', machine.topOfStack());
        machine.step();


        machine.steps(6);
        //Argument checking
        int arg0, arg0b, arg1, arg1b;

        arg0b = machine.getLocalVariable(3);
        arg0 = machine.getLocalVariable(1);

        arg1b = machine.getLocalVariable(4);
        arg1 = machine.getLocalVariable(2);

        assertEquals("var is decremented by 1", arg0b, arg0 + 1);
        assertEquals("var is incremented by 2", arg1b, arg1 - 2);

        //passed ok?
        machine.step();
        assertEquals("Top of stack should be 0xDEAD", 0xDEAD, machine.topOfStack());
        machine.step();
        assertEquals("Top of stack should be " + arg0, arg0, machine.topOfStack());
        machine.step();
        assertEquals("Top of stack should be " + arg1, arg1, machine.topOfStack());
        machine.step();

        assertEquals("PC should be 0x17", 0x17, machine.getProgramCounter());

        //recursive call to diamond
        machine.steps(8); //call print
        assertEquals("Expected INVOKEVIRTUAL", INVOKEVIRTUAL, machine.getInstruction());
        machine.steps(12);
        assertEquals("Expected INVOKEVIRTUAL", INVOKEVIRTUAL, machine.getInstruction());
        machine.steps(12);
        assertEquals("Expected INVOKEVIRTUAL", INVOKEVIRTUAL, machine.getInstruction());

        machine.steps(7);
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.steps(2);
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.steps(2);
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());

        machine.steps(4); //call print
        assertEquals("Expected INVOKEVIRTUAL", INVOKEVIRTUAL, machine.getInstruction());

        machine.steps(12);
        assertEquals("Expected INVOKEVIRTUAL", INVOKEVIRTUAL, machine.getInstruction());

        machine.steps(12);
        assertEquals("Expected INVOKEVIRTUAL", INVOKEVIRTUAL, machine.getInstruction());

        machine.steps(12);
        assertEquals("Expected INVOKEVIRTUAL", INVOKEVIRTUAL, machine.getInstruction());


        machine.steps(7);
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.steps(2);
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.steps(2);
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.steps(2);
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.step();

        machine.step();
        assertEquals("Top of stack should be \"\\n\"", '\n', machine.topOfStack());
        machine.step();


        machine.steps(9); //diamond recursive call
        assertEquals("Excpected INVOKEVIRTUAL", INVOKEVIRTUAL, machine.getInstruction());
        machine.step();
        //random PC check
        assertEquals("PC should be 0x17", 0x17, machine.getProgramCounter());


        machine.steps(100); // go straight to the print char returns. We expect 6
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.steps(2);
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.steps(2);
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.steps(2);
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.steps(2);
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.steps(2);
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.step();

        machine.step(); // print line
        assertEquals("Top of stack should be \"\\n\"", '\n', machine.topOfStack());
        machine.step();


        // We are at this point
        //    3
        //   222
        //  11111


        machine.steps(9);
        assertEquals("Excpected INVOKEVIRTUAL", INVOKEVIRTUAL, machine.getInstruction());
        machine.step();

        machine.steps(12); // this invoke should return without any recursive calls
        assertEquals("Expected IRETURN", IRETURN, machine.getInstruction());
        machine.step();

        invokes = returns = 0;
        while(machine.getInstruction() != HALT){
            if(machine.getInstruction() == INVOKEVIRTUAL){
                invokes ++;
            }
            if(machine.getInstruction() == IRETURN){
                returns ++;
            }
            machine.step();
        }


        assertEquals("Expected 13 more calls to INVOKEVIRTUAL", 13, invokes);

        assertEquals("Expected 15 more calls to IRETURN", 15, returns);

    }



}