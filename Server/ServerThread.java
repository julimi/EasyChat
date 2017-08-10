import javax.swing.*;
import java.io.*;
import java.net.Socket;

/**
 * Created by Julimi on 2017/8/9.
 */

// using a thread to handle with I/O stream
public class ServerThread extends Thread {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private BufferedReader reader;
    private JTextField jtfsend;
    private JTextArea jtext;
    private String chatlog = "Successfully connect to the client!\r\n";

    public ServerThread(Socket socket, JTextField jtfsend, JTextArea jtext) throws IOException {
        this.socket = socket;
        this.jtfsend = jtfsend;
        this.jtext = jtext;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }

    // convert inputStream to string
    public String readString(InputStream in) throws IOException {
        reader = new BufferedReader(new InputStreamReader(in));
        return reader.readLine();
    }

    // catch the message and put given string into the chatting area
    private void processChat() throws IOException {
        // in Windows, \r\n means return and newline
        String hello = "Hello, Welcome to EasyQQ Server!\r\n";
        byte[] data = hello.getBytes();
        out.write(data);
        out.flush();

        String input = readString(in);

        // set a condition for disconnect the connection between server and client
        //boolean flag = true;
        while (input!=null) {
            chatlog += "Client: " + input + "\r\n";
            jtext.setText(chatlog);
            input = readString(in);
        }

        // disconnect and say goodbye to client
        String bye = "Byebye!\r\n";
        data = bye.getBytes();
        out.write(data);
        out.flush();
        socket.close();
    }

    // send message
    public void send(String s) {
        String temp = "Server: " + s + "\r\n";
        chatlog += temp;
        jtext.setText(chatlog);

        // send
        String temp1 = s + "\r\n";
        try {
            byte[] data = temp1.getBytes();
            out.write(data);
        } catch (IOException e) {
            System.out.println("Server fails to send message!!");
        }
    }

    public void run() {
        try {
            processChat();
        } catch (IOException e) {
            System.out.println("Fail to connect!!!");
        }
    }
}
