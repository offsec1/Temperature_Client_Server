package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AbstractUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

    protected static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private int port;
    private ServerSocket ss;
    private Socket socket;

    public Server() {
        try {
            this.port = Integer.parseInt(AbstractUtils.SETTINGS.get("SERVER_PORT"));
        } catch (Exception ex) {
            LOG.error("initialising client handler failed", ex);
        }
    }

    public void run() {
        try {
            ss = new ServerSocket(port);

            while (true) {
                socket = ss.accept();

                System.out.println("A new client is connected : " + socket);

                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(socket, dis, dos);

                // Invoking the start() method
                t.start();
            }

        } catch (IOException ex) {
            LOG.error("creating client handler failed", ex);
            LOG.debug("trying to close socket");
            try {
                socket.close();
            } catch (IOException e) {
                LOG.error("closing socket failed", e);
            }
        }
    }

}
