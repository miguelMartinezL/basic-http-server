import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MyHttpHandler implements HttpHandler {
    Controller controller = new Controller();
    ProductService productService = new ProductService();

    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue="";
        if("GET".equals(httpExchange.getRequestMethod())){
            if("products".equals(httpExchange.getRequestURI().toString().split("/")[1])) {
                requestParamValue = handleGetRequest(httpExchange);
            }
            OutputStream outputStream = httpExchange.getResponseBody();
            if(!(requestParamValue.isEmpty())) {
                String htmlResponse =  requestParamValue;

                // this line is a must
                httpExchange.sendResponseHeaders(200, htmlResponse.length());
                outputStream.write(htmlResponse.getBytes());
                outputStream.flush();
                outputStream.close();
            } else {
                String htmlResponse = "404 Not Found";
                httpExchange.sendResponseHeaders(404, htmlResponse.length());
                outputStream.write(htmlResponse.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        }else if("POST".equals(httpExchange.getRequestMethod())) {
            if("products".equals(httpExchange.getRequestURI().toString().split("/")[1])) {
                requestParamValue = handlePostRequest(httpExchange);
                //requestParamValue = httpExchange.getRequestURI().toString().split("//")[0];
            }
            OutputStream outputStream = httpExchange.getResponseBody();
            if(!(requestParamValue.isEmpty())) {
                String htmlResponse =  requestParamValue;
                // this line is a must
                httpExchange.sendResponseHeaders(201, htmlResponse.length());
                outputStream.write(htmlResponse.getBytes());
                outputStream.flush();
                outputStream.close();
            } else {
                String htmlResponse = "403 Forbidden";
                httpExchange.sendResponseHeaders(403, htmlResponse.length());
                outputStream.write(htmlResponse.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        }else if ("DELETE".equals(httpExchange.getRequestMethod())) {
            if("products".equals(httpExchange.getRequestURI().toString().split("/")[1])) {
                requestParamValue = handleDeleteRequest(httpExchange);
            }
            OutputStream outputStream = httpExchange.getResponseBody();
            if(!(requestParamValue.isEmpty())) {
                String htmlResponse =  requestParamValue;

                // this line is a must
                httpExchange.sendResponseHeaders(200, htmlResponse.length());
                outputStream.write(htmlResponse.getBytes());
                outputStream.flush();
                outputStream.close();
            } else {
                String htmlResponse = "404 Not Found";
                httpExchange.sendResponseHeaders(404, htmlResponse.length());
                outputStream.write(htmlResponse.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        }
//        else if ("PUT".equals(httpExchange.getRequestMethod())) {
//            if ("updproduct".equals(httpExchange.getRequestURI().toString().split("/")[1])) {
//                requestParamValue = handleUpdateRequest(httpExchange);
//            }
//        }
      //  handleResponse(httpExchange, requestParamValue);
    }
    private String handleGetRequest(HttpExchange httpExchange)
    {
        String uri = httpExchange.getRequestURI().toString();
        int uriSize = httpExchange.getRequestURI().toString().length();
        return controller.getController(uri, uriSize);
    }

    private String handlePostRequest(HttpExchange httpExchange) throws IOException {
        StringBuilder jsonBuff = new StringBuilder();
        try{
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while((line = br.readLine()) != null){
                jsonBuff.append(line);
            }
        } catch (Exception e ){
            return "error reading";
        }

        return controller.postController(String.valueOf(jsonBuff));
    }
    private String handleDeleteRequest(HttpExchange httpExchange) throws IOException{
        boolean check = false;
        if (httpExchange.getRequestURI().toString().length() > 10) {
            String id = httpExchange.getRequestURI().toString().split("/")[2];
            check = productService.deleteOne(Integer.parseInt(id));
        }

//        Map<String, Object> parameters = new HashMap<String, Object>();
//        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
//
//        BufferedReader br = new BufferedReader(isr);
//        String query = br.readLine();
//        parseQuery(query, parameters);
//        int id = Integer.parseInt(parameters.get("id").toString());
//        boolean check = productService.deleteOne(id);
        String response = "";
        if (!check) {
            response = "Product Deleted";
        }
        return response;
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
    public static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {

        if (query != null){
            String pairs[] = query.split("[&]");
            for (String pair : pairs) {
                String param[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                            System.getProperty("file.encoding"));
                }

                if(param.length > 1){
                    value = URLDecoder.decode(param[1],
                            System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);
                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException{
        OutputStream outputStream = httpExchange.getResponseBody();
        if(!(requestParamValue.isEmpty())) {
            String htmlResponse =  requestParamValue;

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
