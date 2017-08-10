import org.omg.IOP.IOR;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

/**
 * Created by Julimi on 2017/8/9.
 */
public class ClientThread extends Thread {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private BufferedReader reader;
    private JTextArea jtext;
    private JTextField jtfsend;
    //public boolean connected;
    private String chatlog = "Successful to log into server!\r\n";

    public ClientThread(Socket socket, JTextArea jtext, JTextField jtfsend) throws IOException {
        this.socket = socket;
        this.jtext = jtext;
        this.jtfsend = jtfsend;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        //this.connected = true;
    }

    public String readString(InputStream in) throws IOException {
        reader = new BufferedReader(new InputStreamReader(in));
        return reader.readLine();
    }

    public void disconnect() throws IOException {
        String bye = "Byebye!\r\n";
        byte[] data = bye.getBytes();
        out.write(data);
        out.flush();
        socket.close();
    }

    public void read() throws IOException {
        String input = readString(in);
        while (true) {
            chatlog += "Server: " + input + "\r\n";
            jtext.setText(chatlog);
            input = readString(in);
        }
        //socket.close();
    }

    public void send(String s) {
        String temp = s + "\r\n";
        chatlog += "Client: ";
        chatlog += temp;
        jtext.setText(chatlog);
        try {
            byte[] data = temp.getBytes();
            out.write(data);

        } catch (IOException e) {
            System.out.println("Client fails to send message!!");
        }
    }

    public void run() {
        try {
            read();
        } catch (IOException e) {
            System.out.println("Client fails to read the message!!");
        }
    }
}
