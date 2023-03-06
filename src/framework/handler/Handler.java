package framework.handler;

import application.Product;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import framework.Json;
import framework.MessageLogger;
import framework.context.ComponentScanner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Handler implements HttpHandler{
    private final Map<String, Method> methods = ComponentScanner.getRestControllerMethods();

    public void handle(HttpExchange httpExchange)
    {

        String reqMethod = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();


        Method method = methods.get(reqMethod + path);
        Class<?> cls = method.getDeclaringClass();
        Object controller = ComponentScanner.getBean(cls);

        switch(reqMethod){
            case "GET":
                try{
                    Object obj;
                    obj = method.invoke(controller);
                    String json = Json.ArrToJson(obj);
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
                    InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
                    StringBuilder jsonBuff = new StringBuilder();
                        BufferedReader br = new BufferedReader(isr);
                        String line;
                        while ((line = br.readLine()) != null)
                        {
                            jsonBuff.append(line);
                        }
                        Object param = Json.fromJson(jsonBuff.toString(), Product.class);
                        Object obj = method.invoke(controller, param);
                        String json = Json.toJson(obj);

                        httpExchange.sendResponseHeaders(200,json.length());
                        OutputStream outputStream = httpExchange.getResponseBody();
                        outputStream.write(json.getBytes());
                        outputStream.flush();
                    outputStream.close();
                } catch (Exception e){
                    MessageLogger.error(e.getMessage());
                }
        }
    }
}
