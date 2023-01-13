import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

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

    public String postController(InputStream body, int uriSize)
    {
        if(uriSize < 10)
        {
            StringBuilder jsonBuff = new StringBuilder();
            try
            {
                InputStreamReader isr = new InputStreamReader(body, "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null)
                {
                    jsonBuff.append(line);
                }
            } catch (Exception e)
                {
                    return "error reading";
                }
            String response = productService.addOne(String.valueOf(jsonBuff));
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
            int id = Integer.parseInt(uri.toString().split("/")[2]);
            return (productService.deleteOne(id) == true ? "Product Deleted":"");
        }
        return (productService.deleteAll() == true ? "All products Deleted":"");
    }
}
