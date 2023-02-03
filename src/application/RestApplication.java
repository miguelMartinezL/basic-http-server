package application;
import framework.annotation.ScanApp;
import framework.Server;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ScanApp(packages = {"framework","application"})
public class RestApplication {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run(RestApplication.class, args);
    }
}
