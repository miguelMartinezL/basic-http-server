import java.util.List;

public class Controller {
    private ProductInterface productService;

    public List<Product> getProducts() {
        List<Product> list = productService.findAll();
        return list;
    }
}
