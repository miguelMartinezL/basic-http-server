package application;
import framework.Server;

import java.io.IOException;

public class RestApplication {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}
