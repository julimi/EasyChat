import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Julimi on 2017/8/9.
 */
public class Server extends JFrame implements ActionListener {
    JTextArea jtext;
    JScrollPane jscroll;
    JTextField jtfsend;
    JButton sendButton;
    //JButton disconnectButton;
    //int port = 1;
    ServerSocket server;
    Socket socket;
    ServerThread sthread;

    Server(int port) throws IOException {
        super("Server");
        this.setSize(500,500);
        this.setLayout(new FlowLayout());
        

        // chatting area
        jtext = new JTextArea(10,40);
        jscroll = new JScrollPane(jtext);
        this.add(jscroll);

        // the message
        jtfsend = new JTextField(40);   // same as column of text area
        this.add(jtfsend);

        // button for send
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);
        this.add(sendButton);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        // create server
        server = new ServerSocket(port);
        System.out.println("Successful to create server" + port);

        // create a socket
        // let server connect to client repeatedly
        while (true) {
            System.out.println("Wating for client...");
            // pick one connect from waiting list to make server stuck
            socket = server.accept();
            System.out.println("Client logged in!");
            sthread = new ServerThread(socket,jtfsend,jtext);
            sthread.start();
            System.out.println("Thread loops once");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = jtfsend.getText();
        sthread.send(s);
        jtfsend.setText("");
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(8888);
    }
}
