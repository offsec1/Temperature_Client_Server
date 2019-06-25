package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AbstractUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    protected static final Logger LOG = LoggerFactory.getLogger(Client.class);

    private int port;
    private String ipString;

    public Client() {
        try {
            this.port = Integer.parseInt(AbstractUtils.SETTINGS.get("SERVER_PORT"));
            this.ipString = AbstractUtils.SETTINGS.get("SERVER_IP");
        } catch (Exception ex) {
            LOG.error("initialising client failed", ex);
        }
    }

    public void run() {
        try {
            Scanner sc = new Scanner(System.in);
            Socket socket = new Socket(InetAddress.getByName(ipString), port);

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // the following loop performs the exchange of
            // information between client and client handler
            while (true) {
                System.out.println(dis.readUTF());
                String sendString = sc.nextLine();
                dos.writeUTF(sendString);

                // If client sends exit,close this connection
                // and then break from the while loop
                if (sendString.equals("Exit")) {
                    System.out.println("Closing this connection : " + socket);
                    socket.close();
                    System.out.println("Connection closed");
                    break;
                }

                // printing response
                String response = dis.readUTF();
                System.out.println(response);
            }

            sc.close();
            dis.close();
            dos.close();
        } catch (IOException ex) {
            LOG.error("message exchange failed", ex);
        }
    }

}
