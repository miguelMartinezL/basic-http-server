package application;
import framework.annotation.ScanApp;
import framework.Server;

import java.io.IOException;

@ScanApp(packages = {"application"})
public class RestApplication {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run(RestApplication.class, args);
    }
}
