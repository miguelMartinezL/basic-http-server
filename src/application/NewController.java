package application;
import framework.annotation.GetMapping;
import framework.annotation.PostMapping;

import java.util.List;


public class NewController {
    // agregar anotacion autowired
    ProductService productService ;
    @GetMapping(value = "/products")
    public List getProducts(){
        return productService.get();
    }

    @GetMapping("/products/{id}")
    public Product getByid(int id) {return productService.get(id);}


}
