import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

public class Server {
    public static void main(String[] args) throws Exception {
        final int port = 8001;
        final String hostName = "localhost";
        HttpServer server = HttpServer.create( new InetSocketAddress(hostName, port), 0);

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        server.createContext("/", new MyHttpHandler());
        server.setExecutor(threadPoolExecutor);
        server.start();
        Logger logger = Logger.getLogger(Server.class.getName());
        logger.info(" Server started on port: " + port);
    }
}
