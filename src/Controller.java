import java.util.List;

public class Controller {
    private ProductInterface productService;

    public List<String> getProducts() {
        ProductService list = new ProductService();
        List<String> list1 = list.findAll();
        return list1;
    }

}
