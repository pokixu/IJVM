package pad.ijvm;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Deque;
import java.nio.ByteBuffer;

/**
 * Created by Polina on 15-Jun-17.
 */
public class InstructionsIJVM {
    int intToStack;
    Integer arg1, arg2;
    String currentInstruction;
    boolean running;

    InstructionsIJVM() {
        intToStack = 0;
        arg1 = null;
        arg2 = null;

        currentInstruction = "";
        running = true;
    }

    void bipush(byte[] instructions, int pc, Deque<Integer> stack) {
        currentInstruction = "BIPUSH";
        intToStack = (int) instructions[pc + 1];
        stack.addFirst(intToStack);
    }

    void dup(Deque<Integer> stack) {
        currentInstruction = "DUP";
        intToStack = stack.peekFirst();
        stack.addFirst(intToStack);
    }

    void err() {
        currentInstruction = "ERR";
        System.err.println("Error message.");
        running = false;
    }

    int goTo(byte[] instructions, int pc) {
        currentInstruction = "GOTO";

        byte[] arr = new byte[2];
        arr[0] = instructions[pc + 1];
        arr[1] = instructions[pc + 2];

        ByteBuffer wrapped = ByteBuffer.wrap(arr);
        short offset = wrapped.getShort();

        return offset;
    }

    void halt() {
        currentInstruction = "HALT";
        running = false;
    }

    void iAdd(Deque<Integer> stack) {
        currentInstruction = "IADD";
        arg1 = stack.removeFirst();
        arg2 = stack.removeFirst();

        intToStack = arg1 + arg2;
        stack.addFirst(intToStack);
    }

    void iAnd(Deque<Integer> stack) {
        currentInstruction = "IAND";
        arg1 = stack.removeFirst();
        arg2 = stack.removeFirst();

        intToStack = arg1 & arg2;
        stack.addFirst(intToStack);
    }

    int ifEq(byte[] instructions, int pc, Deque<Integer> stack) {
        currentInstruction = "IFEQ";
        arg1 = stack.removeFirst();
        if (arg1 == 0) {
            byte[] arr = new byte[2];
            arr[0] = instructions[pc + 1];
            arr[1] = instructions[pc + 2];

            ByteBuffer wrapped = ByteBuffer.wrap(arr);
            short offset = wrapped.getShort();

            return offset;
        }
        else {
            return 3;
        }
    }

    int ifLt(byte[] instructions, int pc, Deque<Integer> stack) {
        currentInstruction = "IFLT";
        arg1 = stack.removeFirst();
        if (arg1 < 0) {
            byte[] arr = new byte[2];
            arr[0] = instructions[pc + 1];
            arr[1] = instructions[pc + 2];

            ByteBuffer wrapped = ByteBuffer.wrap(arr);
            short offset = wrapped.getShort();

            return offset;
        }
        else {
            return 3;
        }
    }

    int ifIcmpeq(byte[] instructions, int pc, Deque<Integer> stack) {
        currentInstruction = "IF_ICMPEQ";
        arg1 = stack.removeFirst();
        arg2 = stack.removeFirst();
        if (arg1 == arg2) {
            byte[] arr = new byte[2];
            arr[0] = instructions[pc + 1];
            arr[1] = instructions[pc + 2];

            ByteBuffer wrapped = ByteBuffer.wrap(arr);
            short offset = wrapped.getShort();

            return offset;
        }
        else {
            return 3;
        }
    }

    int iInc(byte[] instructions, int pc) {
        currentInstruction = "IINC";
        return (int) instructions[pc + 2];
    }

    int iLoad(byte[] instructions, int pc) {
        currentInstruction = "ILOAD";
        return 0xFF & instructions[pc + 1];
    }

    void in(InputStream in, Deque<Integer> stack) {
        currentInstruction = "IN";
        try {
            int num = in.read();
            if (num == -1) {
                stack.addFirst(0);
            }
            else {
                stack.addFirst(num);
            }
        } catch (IOException e) {
            System.err.printf("%s", e.getMessage());
        }
    }

    int invokeVirtual(byte[] instructions, int pc, byte[] constants, Deque<Integer> newFrameStack, Deque<Integer> stack) {
        currentInstruction = "INVOKEVIRTUAL";
        // getting disp
        byte[] arrDisp = new byte[2];
        arrDisp[0] = instructions[pc + 1];
        arrDisp[1] = instructions[pc + 2];

        ByteBuffer wrapped = ByteBuffer.wrap(arrDisp);
        short disp = wrapped.getShort();

        // getting the new PC from constPool by disp index
        byte[] arrPC = new byte[4];
        arrPC[0] = constants[disp * 4];
        arrPC[1] = constants[disp * 4 + 1];
        arrPC[2] = constants[disp * 4 + 2];
        arrPC[3] = constants[disp * 4 + 3];

        ByteBuffer constWrapped = ByteBuffer.wrap(arrPC);
        pc = constWrapped.getInt();

        // going to the beginning of the instructions for the new method
        byte[] arrNumPars = new byte[2];
        arrNumPars[0] = instructions[pc];
        arrNumPars[1] = instructions[pc + 1];
        ByteBuffer wrappedNumPars = ByteBuffer.wrap(arrNumPars);
        short numParameters = wrappedNumPars.getShort();

        byte[] arrVarAreaSize = new byte[2];
        arrVarAreaSize[0] = instructions[pc + 2];
        arrVarAreaSize[1] = instructions[pc + 3];
        ByteBuffer wrappedVarArea = ByteBuffer.wrap(arrVarAreaSize);
        short localVarAreaSize = wrappedVarArea.getShort();

        // moving the needed number of parameters from the previous stack to the stack of the new method
        int[] tempArr = new int[numParameters];

        for (int i = 0; i < numParameters; i ++) {
            tempArr[i] = stack.removeFirst();
        }
        for (int i = tempArr.length - 1; i >= 0; i --) {
            newFrameStack.addFirst(tempArr[i]);
        }

        return pc;
    }

    void iOr(Deque<Integer> stack) {
        currentInstruction = "IOR";
        arg1 = stack.removeFirst();
        arg2 = stack.removeFirst();

        intToStack = arg1 | arg2;
        stack.addFirst(intToStack);
    }

    int iReturn(Deque<Frame> frameStack, Frame toExit) {
        currentInstruction = "IRETURN";
        int oldPC = toExit.programCounter;

        frameStack.removeFirst();
        Frame oldFrame = frameStack.peekFirst();

        Integer tos;

        if (!toExit.stack.isEmpty()) {
            tos = toExit.stack.removeFirst();
            oldFrame.stack.addFirst(tos);
        }
        else {
            oldFrame.stack.addFirst(0);
        }

        return oldPC;
    }

    int iStore(byte[] instructions, int pc) {
        currentInstruction = "ISTORE";
        return 0xFF & instructions[pc + 1];
    }

    void iSub(Deque<Integer> stack) {
        currentInstruction = "ISUB";
        arg1 = stack.removeFirst();
        arg2 = stack.removeFirst();

        intToStack = arg2 - arg1;
        stack.addFirst(intToStack);
    }

    void ldcw(byte[] constants, byte[] instructions, int pc, Deque<Integer> stack) {
        currentInstruction = "LDC_W";
        byte[] indexArr = new byte[2];
        indexArr[0] = instructions[pc + 1];
        indexArr[1] = instructions[pc + 2];

        ByteBuffer indexWrapped = ByteBuffer.wrap(indexArr);
        short index = indexWrapped.getShort();

        byte[] constArr = new byte[4];
        constArr[0] = constants[index * 4];
        constArr[1] = constants[index * 4 + 1];
        constArr[2] = constants[index * 4 + 2];
        constArr[3] = constants[index * 4 + 3];

        ByteBuffer constWrapped = ByteBuffer.wrap(constArr);
        int constantToStack = constWrapped.getInt();

        stack.addFirst(constantToStack);
    }

    void nop() {
        currentInstruction = "NOP";
    }

    void out(PrintStream out, Deque<Integer> stack) {
        currentInstruction = "OUT";
        out.print((char) stack.removeFirst().intValue());
    }

    void pop(Deque<Integer> stack) {
        currentInstruction = "POP";
        stack.removeFirst();
    }

    void swap(Deque<Integer> stack) {
        currentInstruction = "SWAP";
        intToStack = stack.removeFirst();
        Integer temp = stack.removeFirst();

        stack.addFirst(intToStack);
        stack.addFirst(temp);
    }

    void wide() {
        currentInstruction = "WIDE";
    }

    int wideIStore(byte[] instructions, int pc) {
        currentInstruction += "\nISTORE";
        byte[] arr = new byte[4];
        arr[0] = 0x00;
        arr[1] = 0x00;
        arr[2] = instructions[pc + 1];
        arr[3] = instructions[pc + 2];

        ByteBuffer wrapped = ByteBuffer.wrap(arr);
        int label = wrapped.getInt();

        return label;
    }

    int wideILoad(byte[] instructions, int pc) {
        currentInstruction += "\nILOAD";
        byte[] arr = new byte[4];
        arr[0] = 0x00;
        arr[1] = 0x00;
        arr[2] = instructions[pc + 1];
        arr[3] = instructions[pc + 2];

        ByteBuffer wrapped = ByteBuffer.wrap(arr);
        int label = wrapped.getInt();

        return label;
    }
}
