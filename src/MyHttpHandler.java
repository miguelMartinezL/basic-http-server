import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;

public class MyHttpHandler implements HttpHandler {
    Controller controller = new Controller();

    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue="";
        if("GET".equals(httpExchange.getRequestMethod())){
            if("products".equals(httpExchange.getRequestURI().toString().split("/")[1])) {
                requestParamValue = handleGetRequest(httpExchange);
            }
        }else if("POST".equals(httpExchange.getRequestMethod())) {
            if("products".equals(httpExchange.getRequestURI().toString().split("/")[1])) {
                requestParamValue = handlePostRequest(httpExchange);
            }
        }else if ("DELETE".equals(httpExchange.getRequestMethod())) {
            if("products".equals(httpExchange.getRequestURI().toString().split("/")[1])) {
                requestParamValue = handleDeleteRequest(httpExchange);
            }
        }
//        else if ("PUT".equals(httpExchange.getRequestMethod())) {
//            if ("updproduct".equals(httpExchange.getRequestURI().toString().split("/")[1])) {
//                requestParamValue = handleUpdateRequest(httpExchange);
//            }
//        }
        handleResponse(httpExchange, requestParamValue);
    }
    private String handleGetRequest(HttpExchange httpExchange)
    {
        String uri = httpExchange.getRequestURI().toString();
        int uriSize = httpExchange.getRequestURI().toString().length();
        return controller.getController(uri, uriSize);
    }
    private String handlePostRequest(HttpExchange httpExchange) throws IOException {
        InputStream reqBody = httpExchange.getRequestBody();
        int uriSize = httpExchange.getRequestURI().toString().length();
        return controller.postController(reqBody, uriSize);
    }
    private String handleDeleteRequest(HttpExchange httpExchange) throws IOException
    {
        String uri = httpExchange.getRequestURI().toString();
        int uriSize = httpExchange.getRequestURI().toString().length();
        return controller.deleteController(uri, uriSize);

    }
//    private String handleUpdateRequest(HttpExchange  httpExchange) throws IOException {
//        Map<String, Object> parameters = new HashMap<String, Object>();
//        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
//
//        BufferedReader br = new BufferedReader(isr);
//        String query = br.readLine();
//        parseQuery(query, parameters);
//        Product response = productService.update(parameters);
//        return ;
//    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException{
        OutputStream outputStream = httpExchange.getResponseBody();
        if(requestParamValue.isEmpty()) {
            int rCode = 404;
            String Response = "404 Page not found";
            switch(httpExchange.getRequestMethod()){
                case "POST":
                    rCode = 403;
                    Response = "403 Forbidden";
            }
            httpExchange.sendResponseHeaders(rCode, Response.length());
            outputStream.write(Response.getBytes());
            outputStream.flush();
            outputStream.close();
        } else {
            String Response =  requestParamValue;
            int rCode = 200;
            switch(httpExchange.getRequestMethod()){
                case "POST":
                    rCode = 403;
            }
            httpExchange.sendResponseHeaders(rCode, Response.length());
            outputStream.write(Response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }
}
