import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

public class server {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create( new InetSocketAddress("localhost", 8001), 0);

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        server.createContext("/test", new MyHttpHandler());
        server.setExecutor(threadPoolExecutor);
        server.start();
        Logger logger = Logger.getLogger(server.class.getName());
        logger.info(" Server started on port: 8001");
    }
}
