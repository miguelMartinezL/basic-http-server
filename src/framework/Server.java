package framework;

import com.sun.net.httpserver.HttpServer;
import framework.handler.Handler;
import framework.handler.MyHttpHandler;
import framework.annotation.*;
import framework.context.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

public class Server {
    public void run(Class<?> source, String... args) throws IOException {
        ScanApp scan = source.getAnnotation(ScanApp.class);
        ComponentScanner.scan(scan.packages());
        final int port = 8001;
        final String hostName = "localhost";
        HttpServer server = HttpServer.create( new InetSocketAddress(hostName, port), 0);

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        server.createContext("/", new Handler());
        server.setExecutor(threadPoolExecutor);
        server.start();
        Logger logger = Logger.getLogger(Server.class.getName());
        logger.info(" framework.Server started on port: " + port);
    }
    public void start() throws IOException {
//        final int port = 8001;
//        final String hostName = "localhost";
//        HttpServer server = HttpServer.create( new InetSocketAddress(hostName, port), 0);
//
//        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
//
//        server.createContext("/", new Handler());
//        server.setExecutor(threadPoolExecutor);
//        server.start();
//        Logger logger = Logger.getLogger(Server.class.getName());
//        logger.info(" framework.Server started on port: " + port);
    }
}
