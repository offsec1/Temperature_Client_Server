package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import temperature.Temperature;
import temperature.TemperatureUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class ClientHandler extends Thread {

    protected static final Logger LOG = LoggerFactory.getLogger(Server.class);

    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        String received;
        String response;

        while (true) {
            try {

                // Ask user what he wants
                dos.writeUTF("For which date do you want to know the temperature?[dd.mm.yyyy]..\n" +
                        "Type Exit to terminate connection.");

                // receive the answer from client
                received = dis.readUTF();

                if (received.equals("Exit")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }

                //if input is valid
                if (TemperatureUtils.validateDate(received)) {
                    List<Temperature> t = TemperatureUtils.getTemperature(received);
                    if (t.size() > 0) {
                        response = t.stream().map(Temperature::toString).collect(Collectors.joining(" | "));
                        dos.writeUTF(response); //send response to client
                    } else {
                        dos.writeUTF("No data found for specified date");
                    }
                } else {
                    dos.writeUTF("invalid input");
                }

            } catch (IOException ex) {
                LOG.error("message exchange failed", ex);
            }
        }

        try {
            this.dis.close();
            this.dos.close();
        } catch (IOException ex) {
            LOG.error("closing streams failed", ex);
        }
    }
}
