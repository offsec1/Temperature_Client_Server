import server.Server;

public class InitServer {
    public static void main(String[] args) throws Exception {
        System.out.println("-------------Server-------------");
        Server server = new Server();
        server.run();
    }
}
