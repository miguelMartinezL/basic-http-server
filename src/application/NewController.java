package application;
import framework.annotation.GetMapping;
import framework.annotation.PostMapping;
import framework.annotation.RequestMapping;
import framework.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/products")
public class NewController {
    // agregar anotacion autowired
    ProductService productService ;
    @GetMapping("/products")
    public List getProducts(){
        return productService.get();
    }

    @GetMapping("/products/{id}")
    public Product getByid(int id) {return productService.get(id);}


}
