import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class MyHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue="";
        if("GET".equals(httpExchange.getRequestMethod())){
            if("product".equals(httpExchange.getRequestURI().toString().split("\\/")[1])) {
                requestParamValue = handleGetRequest(httpExchange);
            }
        }else if("POST".equals(httpExchange.getRequestMethod())) {
            requestParamValue= handlePostRequest(httpExchange);
        }
        handleResponse(httpExchange, requestParamValue);
    }
    private String handleGetRequest(HttpExchange httpExchange) {
        String name = Controller.getName();
        return name;
//            return httpExchange.
//                    getRequestURI()
//                    .toString()
//                    .split("\\?")[1]
//                    .split("=")[1];
    }

    private String handlePostRequest(HttpExchange httpExchange) {
        return httpExchange.
                getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1];
    }
    private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException{
        OutputStream outputStream = httpExchange.getResponseBody();
        if(!requestParamValue.isEmpty()) {
            String htmlResponse = "<html> <body> <h1> Hello " + requestParamValue + "</h1> </body> </html>";

            // this line is a must
            httpExchange.sendResponseHeaders(200, htmlResponse.length());
            outputStream.write(htmlResponse.getBytes());
            outputStream.flush();
            outputStream.close();
        } else {
            String htmlResponse = "<html> <body> <h1> 404</h1> <p>Page not found</p> </body> </html>";
            httpExchange.sendResponseHeaders(404, htmlResponse.length());
            outputStream.write(htmlResponse.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }
}
