package pad.ijvm;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Polina on 21-Jun-17.
 */
public class Frame {
    Deque<Integer> stack;
    int programCounter;

    Frame() {
        stack = new ArrayDeque<>();
        programCounter = 0;
    }
}
