package pad.ijvm;

import pad.ijvm.interfaces.IJVMInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;

public class MachineFactory {

    public static IJVMInterface createIJVMInstance(File binary) throws IOException {
        // Create new machine instance here and return it.

        // 1) Load the binary
        byte[] bytes = new byte[(int) binary.length()];
        FileInputStream fileInputStream = new FileInputStream(binary);
        fileInputStream.read(bytes);
        fileInputStream.close();

        BinaryReader currentBytes = new BinaryReader(bytes);
        ConstantPool constPool = new ConstantPool(currentBytes.constants);
        InstructionPool instrPool = new InstructionPool(currentBytes.instructions);

        // 2) Return the new IJVM instance without starting it.
        return new IJVM(constPool, instrPool);
    }

}
