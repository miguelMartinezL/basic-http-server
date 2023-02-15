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

        for (Map.Entry<String, Method> entry : methods.entrySet()){
            System.out.println("key = " + entry.getKey() + " " + "Method = " + entry.getValue().getName());
        }

        String reqMethod = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        System.out.println(reqMethod + path);

        Method method = methods.get(reqMethod + path);
        Class cls = method.getDeclaringClass();
        Object controller = ComponentScanner.getBean(cls);
        System.out.println(controller.getClass().getSimpleName());

        switch(reqMethod){
            case "GET":
                try{
                    Object obj = method.invoke(controller);
                    String json = Json.toJson(obj);
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
