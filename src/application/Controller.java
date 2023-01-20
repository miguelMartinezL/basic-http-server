package application;

import framework.Json;

import java.io.IOException;
import java.io.InputStream;

public class Controller
{
    ProductService productService = new ProductService();

    public String getController(String uri, int uriSize)
    {
        if (uriSize > 10) {
            int id = Integer.parseInt(uri.split("/")[2]);
            return productService.findById(id);
        }
        return productService.findAll();
    }

    public String postController(InputStream body, int uriSize) throws IOException
    {
        if(uriSize < 10)
        {
            String response = productService.addOne(Json.ioJson(body));
            switch(response){
                case "": return "product already exists";
                case "Error: parsing error": return "bad request";
            }
            return response;
        }
        return "";
    }

    public String deleteController(String uri, int uriSize)
    {
        if (uriSize > 10)
        {
            int id = Integer.parseInt(uri.split("/")[2]);
            return (productService.deleteOne(id) == true ? "application.Product Deleted":"");
        }
        return (productService.deleteAll() == true ? "All products Deleted":"");
    }

    public String updateController(String uri, int uriSize, InputStream body) throws IOException
    {
        if (uriSize > 10)
        {
            int id = Integer.parseInt(uri.split("/")[2]);
            return productService.updateOne(id, Json.ioJson(body));
        }
        return "";
    }
}
