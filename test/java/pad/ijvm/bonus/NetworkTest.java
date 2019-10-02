package pad.ijvm.bonus;

import org.junit.Test;
import pad.ijvm.interfaces.IJVMInterface;
import pad.ijvm.MachineFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.lang.InterruptedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class NetworkTest {

    private String HOST_NAME = "127.0.0.1";
    private int PORT = 5555;

    class IJVMServerRunnable implements Runnable {
        public void run() {
            try {
                IJVMInterface machine = MachineFactory.createIJVMInstance(new File("files/bonus/test_netbind.ijvm"));
                machine.step();
                // load port on stack
                machine.step();
                // Opened connection on port: 5555
                machine.step();
                // Receive a
                machine.step();
                // Receive b
                machine.step();
                // Send b
                machine.step();
                // Send a
                Thread.sleep(1);
                machine.step();
                // closing connection
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test(timeout=5000)
    public void testNetbind() throws IOException, InterruptedException {
        IJVMServerRunnable sr = new IJVMServerRunnable();
        Thread serverThread = new Thread(sr);
        serverThread.start();
        Socket socket = null;
        Thread.sleep(100);
        try {
            socket = new Socket(HOST_NAME, PORT);
        } catch (Exception e) {
            assertNotNull("Failed to connect to IJVM running NETBIND", socket);
        }
        PrintWriter out =
            new PrintWriter(socket.getOutputStream(), true);
        InputStream in = socket.getInputStream();
        out.println("ab");
        Thread.sleep(100);
        char[] buff = new char[2];
        buff[0] = (char) in.read();
        buff[1] = (char) in.read();
        String s = new String(buff);
        assertEquals("The bytes received from the connection should be same bytes that were sent", "ba", s);
        socket.close();
    }

    class IJVMClientRunnable implements Runnable {
        public void run() {
            try {
                IJVMInterface machine = MachineFactory.createIJVMInstance(new File("files/bonus/test_netconnect.ijvm"));
                machine.step();
                // load port on stack
                machine.step();
                // load host on stack
                machine.step();
                // Opened connection on localhost port: 5555
                machine.step();
                // Receive a
                machine.step();
                // Receive b
                machine.step();
                // Send b
                machine.step();
                // Send a
                Thread.sleep(1);
                machine.step();
                // closing connection
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test(timeout=5000)
    public void testNetconnect() throws IOException, InterruptedException {
        IJVMClientRunnable sr = new IJVMClientRunnable();
        Thread clientThread = new Thread(sr);
        clientThread.start();
        Socket socket = null;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            socket = serverSocket.accept();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep(100);
        PrintWriter out =
            new PrintWriter(socket.getOutputStream(), true);
        out.println("ab");
        InputStream in = socket.getInputStream();
        char[] buff = new char[2];
        buff[0] = (char) in.read();
        buff[1] = (char) in.read();
        String s = new String(buff);
        assertEquals("The bytes received from the connection should be same bytes that were sent", "ba", s);
        socket.close();
        serverSocket.close();
    }

}
