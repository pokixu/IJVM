package pad.ijvm;

import pad.ijvm.interfaces.IJVMInterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Deque;
import java.util.ArrayDeque;

/**
 * Created by Polina on 09-Jun-17.
 */

public class IJVM implements IJVMInterface {
    ConstantPool constPool;
    InstructionPool instrPool;
    InstructionsIJVM instrSet;

    Frame currentFrame;
    Integer[] currentFrameArr;
    Deque<Frame> frameStack;

    private InputStream in;
    private PrintStream out;

    IJVM(ConstantPool constPool, InstructionPool instrPool) {
        this.constPool = constPool;
        this.instrPool = instrPool;
        instrSet = new InstructionsIJVM();

        currentFrame = new Frame();
        currentFrameArr = new Integer[500000];
        frameStack = new ArrayDeque<>();
        frameStack.addFirst(currentFrame);

        out = System.out;
        in = System.in;
    }

    public int topOfStack(){
        return currentFrame.stack.peekFirst();
    }

    public int[] getStackContents(){
        Integer[] stackContents;
        stackContents = currentFrame.stack.toArray(new Integer[0]);

        int[] stackContentsNum = new int[stackContents.length];
        for (int i = 0; i < stackContents.length; i++) {
            stackContentsNum[i] = stackContents[i];
        }

        return stackContentsNum;
    }

    public byte[] getText(){
        return instrPool.instructions;
    }

    public byte[] getConstants(){
        return constPool.constants;
    }

    public int getProgramCounter(){
        return instrPool.programCounter;
    }

    public int getLocalVariable(int i) {
        return currentFrameArr[i];
    }

    public int getConstant(int i){
        return (int) constPool.constants[i];
    }

    public byte getInstruction(){
        return instrPool.instructions[instrPool.programCounter];
    }

    void stackToArray(Deque<Integer> stack) {
        Deque<Integer> tempStack = new ArrayDeque<>();

        int[] tempArr = new int[stack.size()];
        for (int i = 0; i < tempArr.length; i ++) {
            tempArr[i] = stack.removeFirst();
            tempStack.addFirst(tempArr[i]);
        }
        for (int i = tempArr.length - 1; i >= 0; i --) {
            stack.addFirst(tempArr[i]);
        }

        Integer[] tempFrameArr = tempStack.toArray(new Integer[0]);
        for (int i = 0; i < tempFrameArr.length; i ++) {
            currentFrameArr[i] = tempFrameArr[i];
        }
    }

    public void step() throws IOException {
        currentFrame = frameStack.peekFirst();
        switch (getInstruction() & 0xFF) {
            case 0x10:
                instrSet.bipush(getText(), getProgramCounter(), currentFrame.stack);
                instrPool.incrementPrCounter(2);
                break;
            case 0x59:
                instrSet.dup(currentFrame.stack);
                instrPool.incrementPrCounter(1);
                break;
            case 0xFE:
                instrSet.err();
                instrPool.incrementPrCounter(1);
                break;
            case 0xA7:
                instrPool.incrementPrCounter(instrSet.goTo(getText(), getProgramCounter()));
                break;
            case 0xFF:
                instrSet.halt();
                instrPool.incrementPrCounter(1);
                break;
            case 0x60:
                instrSet.iAdd(currentFrame.stack);
                instrPool.incrementPrCounter(1);
                break;
            case 0x7E:
                instrSet.iAnd(currentFrame.stack);
                instrPool.incrementPrCounter(1);
                break;
            case 0x99:
                instrPool.incrementPrCounter(instrSet.ifEq(getText(), getProgramCounter(), currentFrame.stack));
                break;
            case 0x9B:
                instrPool.incrementPrCounter(instrSet.ifLt(getText(), getProgramCounter(), currentFrame.stack));
                break;
            case 0x9F:
                instrPool.incrementPrCounter(instrSet.ifIcmpeq(getText(), getProgramCounter(), currentFrame.stack));
                break;
            case 0x84:
                currentFrameArr[(int) instrPool.instructions[getProgramCounter() + 1]] += instrSet.iInc(getText(), getProgramCounter());
                instrPool.incrementPrCounter(3);
                break;
            case 0x15:
                currentFrame.stack.addFirst(currentFrameArr[instrSet.iLoad(getText(), getProgramCounter())]);
                instrPool.incrementPrCounter(2);
                break;
            case 0xFC:
                instrSet.in(in, currentFrame.stack);
                instrPool.incrementPrCounter(1);
                break;
            case 0xB6:
                Frame tempFrame = new Frame();
                tempFrame.programCounter = getProgramCounter();
                frameStack.addFirst(tempFrame);

                instrPool.programCounter = instrSet.invokeVirtual(getText(), getProgramCounter(), getConstants(), tempFrame.stack, currentFrame.stack);
                stackToArray(tempFrame.stack);
                instrPool.incrementPrCounter(4);
                break;
            case 0xB0:
                instrSet.iOr(currentFrame.stack);
                instrPool.incrementPrCounter(1);
                break;
            case 0xAC:
                instrPool.programCounter = instrSet.iReturn(frameStack, currentFrame);
                currentFrame = frameStack.peekFirst();
                instrPool.incrementPrCounter(3);
                break;
            case 0x36:
                currentFrameArr[instrSet.iStore(getText(), getProgramCounter())] = currentFrame.stack.removeFirst();
                instrPool.incrementPrCounter(2);
                break;
            case 0x64:
                instrSet.iSub(currentFrame.stack);
                instrPool.incrementPrCounter(1);
                break;
            case 0x13:
                instrSet.ldcw(getConstants(), getText(), getProgramCounter(), currentFrame.stack);
                instrPool.incrementPrCounter(3);
                break;
            case 0x00:
                instrSet.nop();
                instrPool.incrementPrCounter(1);
                break;
            case 0xFD:
                instrSet.out(out, currentFrame.stack);
                out.flush();
                instrPool.incrementPrCounter(1);
                break;
            case 0x57:
                instrSet.pop(currentFrame.stack);
                instrPool.incrementPrCounter(1);
                break;
            case 0x5F:
                instrSet.swap(currentFrame.stack);
                instrPool.incrementPrCounter(1);
                break;
            case 0xC4:
                instrSet.wide();
                instrPool.incrementPrCounter(1);
                switch (getInstruction() & 0xFF) {
                    case 0x36:
                        currentFrameArr[instrSet.wideIStore(getText(), getProgramCounter())] = currentFrame.stack.removeFirst();
                        break;
                    case 0x15:
                        currentFrame.stack.addFirst(currentFrameArr[instrSet.wideILoad(getText(), getProgramCounter())]);
                }
                instrPool.incrementPrCounter(3);
                break;
            default:
                break;
        }
        //   System.out.println(instrSet.currentInstruction);
    }

   public void run(){
        while((getProgramCounter() < getText().length) && instrSet.running) {
            try {
                step();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       for (int i = 0; i < getStackContents().length; i++) {
            out.println(getStackContents()[i]);
       }
   }

   public void setOutput(PrintStream out){
        this.out = out;
   }

    public void setInput(InputStream in){
        this.in = in;
    }
}
