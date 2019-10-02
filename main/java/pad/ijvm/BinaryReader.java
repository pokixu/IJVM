package pad.ijvm;

import javax.naming.BinaryRefAddr;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by Polina on 11-Jun-17.
 */
public class BinaryReader {
    byte[] magicNum;
    byte[] memoryConst;
    byte[] constLength;
    byte[] constants;
    byte[] memoryInstr;
    byte[] instrLength;
    byte[] instructions;

    static final int BYTES_MAGIC = 4,
                     BYTES_MEMORY = 4,
                     BYTES_LENGTH = 4,
                     BYTES_OFFSET = BYTES_LENGTH + BYTES_MEMORY + BYTES_MAGIC;

    BinaryReader(byte[] file) {
        magicNum = Arrays.copyOfRange(file,
                0,
                BYTES_MAGIC);
        memoryConst = Arrays.copyOfRange(file,
                BYTES_MAGIC,
                BYTES_MAGIC + BYTES_MEMORY);
        constLength = Arrays.copyOfRange(file,
                BYTES_MAGIC + BYTES_MEMORY,
                BYTES_OFFSET);

        ByteBuffer bbc = ByteBuffer.wrap(constLength);
        int constLength = bbc.getInt();

        constants = Arrays.copyOfRange(file,
                BYTES_OFFSET,
                BYTES_OFFSET + constLength);
        memoryInstr = Arrays.copyOfRange(file,
                BYTES_OFFSET + constLength,
                BYTES_OFFSET + constLength + BYTES_MEMORY);
        instrLength = Arrays.copyOfRange(file,
                BYTES_OFFSET + constLength + BYTES_MEMORY,
                BYTES_OFFSET + constLength + BYTES_MEMORY + BYTES_LENGTH);

        ByteBuffer bbi = ByteBuffer.wrap(instrLength);
        int instrLength = bbi.getInt();

        instructions = Arrays.copyOfRange(file,
                BYTES_OFFSET + constLength + BYTES_MEMORY + BYTES_LENGTH,
                BYTES_OFFSET + constLength + BYTES_MEMORY + BYTES_LENGTH + instrLength);
    }
}

