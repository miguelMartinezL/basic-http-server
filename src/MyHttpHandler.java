import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyHttpHandler implements HttpHandler {
    //Controller controller = new Controller();
    ProductService productService = new ProductService();
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue="";
        if("GET".equals(httpExchange.getRequestMethod())){
            if("products".equals(httpExchange.getRequestURI().toString().split("/")[1])) {
                requestParamValue = handleGetRequest(httpExchange);
            }
        }else if("POST".equals(httpExchange.getRequestMethod())) {
            if("addproduct".equals(httpExchange.getRequestURI().toString().split("/")[1])) {
                requestParamValue = handlePostRequest(httpExchange);
            }
        }else if ("DELETE".equals(httpExchange.getRequestMethod())) {
            if("delproduct".equals(httpExchange.getRequestURI().toString().split("/")[1])) {
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
    private String handleGetRequest(HttpExchange httpExchange) {
        //List<Product> name = controller.getProducts();
        List<String> name = productService.findAll();
       return name.toString();
    }

    private String handlePostRequest(HttpExchange httpExchange) throws IOException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");

        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        parseQuery(query, parameters);

        int id = Integer.parseInt(parameters.get("id").toString());
        String pname = parameters.get("pname").toString();
        String batchno = parameters.get("batchno").toString();
        double price = Double.parseDouble(parameters.get("price").toString());
        int noofproduct = Integer.parseInt(parameters.get("noofproduct").toString());

        productService.addOne(id, pname, batchno, price, noofproduct);
        String response = "product added";
        return response;
    }
    private String handleDeleteRequest(HttpExchange httpExchange) throws IOException{
        Map<String, Object> parameters = new HashMap<String, Object>();
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");

        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        parseQuery(query, parameters);
        int id = Integer.parseInt(parameters.get("id").toString());
        boolean check = productService.deleteOne(id);
        String response = "";
        if (check) {
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
