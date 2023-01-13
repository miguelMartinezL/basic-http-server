import java.util.List;

public class Controller {
    //private ProductInterface productService;
    ProductService productService = new ProductService();

    public String getController(String uri, int uriSize)
    {
        if (uriSize > 10) {
            int id = Integer.parseInt(uri.split("/")[2]);
            return productService.findById(id);
        }

        String allProducts = productService.findAll();
        return allProducts ;
    }

    public String postController(String jsonBuffer)
    {
        String reponse = productService.addOne(jsonBuffer);
        return (!reponse.isEmpty() ? reponse : "");
    }
}
