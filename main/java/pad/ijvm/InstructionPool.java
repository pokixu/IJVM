package pad.ijvm;


/**
 * Created by Polina on 11-Jun-17.
 */
public class InstructionPool {
    byte[] instructions;
    int programCounter;

    InstructionPool(byte[] newInstructions) {
        instructions = newInstructions;
        programCounter = 0;
    }

    void incrementPrCounter(int num){
        this.programCounter += num;
    }
}
