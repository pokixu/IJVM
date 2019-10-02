package pad.ijvm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {

	// An emulator capable of executing IJVM byte-code 

    static void printUsage() {
        System.out.println("Usage: java Main file.bin");
    }

    public static void main(String args[]) {

        if (args.length < 1) {
            printUsage();
            System.exit(0);
        }
        try {
            File binary = new File(args[0]);

            // Create the ijvm instance
            MachineFactory.createIJVMInstance(binary).run();
        } catch (FileNotFoundException e) {
            System.err.printf("%s\n", e.getMessage());
        } catch (IOException e) {
            System.err.printf("%s\n", e.getMessage());
        }

    }
}
