import client.Client;

public class InitClient {
    public static void main(String[] args) {
        System.out.println("-------------Client-------------");
        Client client = new Client();
        client.run();
    }
}
