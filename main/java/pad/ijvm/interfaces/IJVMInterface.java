package pad.ijvm.interfaces;

import java.io.IOException;
import java.io.PrintStream;
import java.io.InputStream;

/**
 * Created by sirmc on 12/03/16.
 */
public interface IJVMInterface {

    /**
     *
     * @return The word at the top of the stack of the current frame, 
     * interpreted as a signed integer.
     */
    int topOfStack();

    /**
     *
     * @return Returns the stack of the current frame as an array of integers.
     */
    int[] getStackContents();

    /**
     *
     * @return The current loaded program text as an byte array.
     */
    byte[] getText();

    /**
     *
     * @return The value of the program counter (as an offset from the first instruction).
     */
    int getProgramCounter();

    /**
     *
     * @param i, index of variable to obtain.
     * @return Returns the i:th local variable of the current frame.
     */
    int getLocalVariable(int i);

    /**
     *
     * @param i, index of the constant to obtain
     * @return The constant at location i in the constant pool.
     */
    int getConstant(int i);

    /**
     * Step (perform) one instruction and return.
     * In the case of WIDE, perform the whole WIDE_ISTORE or WIDE_ILOAD.
     */
    void step() throws IOException;

    /**
     * Run the vm with the current state until the machine halts.
     */
    void run();

    /**
     * @return The value of the current instruction represented as a byte.
     */
    byte getInstruction();

    /**
     * Sets the standard output of the IJVM instance.
     * @param out, PrintStream to be used for OUT instruction.
     */
    void setOutput(PrintStream out);

    /**
     * Sets the standard input of the IJVM instance.
     * @param in, InputStream to be used for IN instruction.
     */
    void setInput(InputStream in);




    /**
     * You can ignore this method.
     *
     * Default method to conveniently execute multiple steps at once.
     * @param n, The number of steps that should be executed.
     */
    default void steps(int n) {
        for (int i = 0; i < n; i++) {
            try {
                this.step();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
