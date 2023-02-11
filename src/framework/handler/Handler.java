package framework.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import framework.Json;
import framework.MessageLogger;
import framework.context.ComponentScanner;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Map;

public class Handler implements HttpHandler{
    private Map<String, Method> methods = ComponentScanner.getRestControllerMethods();

    public void handle(HttpExchange httpExchange) throws IOException
    {
        String reqMethod = httpExchange.getRequestMethod();
        System.out.println(reqMethod);
        String path = httpExchange.getRequestURI().getPath();
        System.out.println(path);

        Method method = methods.get(reqMethod + path);
        System.out.println(method.getName());
        Class cls = method.getDeclaringClass();
        Object controller = ComponentScanner.getClass(cls);
        MessageLogger.info(controller.getClass().getName());
        MessageLogger.info(method.getName());

        switch(reqMethod){
            case "GET":
                try{
                    Object obj = method.invoke(controller);
                    String json = Json.parseJson(obj);
                    httpExchange.sendResponseHeaders(200,json.length());
                    OutputStream outputStream = httpExchange.getResponseBody();
                    outputStream.write(json.getBytes());
                    outputStream.flush();
                    outputStream.close();


                } catch (Exception e){
                    MessageLogger.error(e.getMessage());
                }
            case "POST":
                try{

                } catch (Exception e){

                }
        }
    }
}
