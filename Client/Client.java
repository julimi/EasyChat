import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Julimi on 2017/8/9.
 */
public class Client<Final> extends JFrame {
    JTextArea jtext;
    JTextField jtfsend;
    JScrollPane jscroll;
    JButton connectButton;
    JButton sendButton;
    JButton disconnectButton;
    Socket socket;
    ClientThread thread;

    public Client() throws IOException {
        super("Client");
        this.setSize(500,500);
        this.setLayout(new FlowLayout());

        jtext = new JTextArea(10,40);
        jscroll = new JScrollPane(jtext);
        this.add(jscroll);

        jtfsend = new JTextField(40);
        this.add(jtfsend);

        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = "192.168.0.28";
                int port = 8888;
                System.out.println("Get ready to connect to the server: " + ip + "port: " + port);
                try {
                    socket = new Socket(ip,port);
                    thread = new ClientThread(socket,jtext,jtfsend);
                    thread.start();
                    System.out.println("Successful to connect!!!");
                } catch (IOException ioe) {
                    System.out.println("Fail to connect!!!");
                }
            }
        });
        this.add(connectButton);

        disconnectButton = new JButton("Disconnect");
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	thread.disconnect();
                } catch (IOException ioe) {
                	// who cares
                }
            }
        });
        this.add(disconnectButton);

        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = jtfsend.getText();
                thread.send(s);
                jtfsend.setText("");
            }
        });
        this.add(sendButton);

        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
    }
}
