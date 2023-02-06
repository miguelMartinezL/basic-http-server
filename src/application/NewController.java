package application;
import framework.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/products")
public class NewController {
    // agregar anotacion autowired
    @Autowired
    ProductService productService ;
    @GetMapping("/products")
    public List getProducts(){
        return productService.get();
    }

    @GetMapping("/products/{id}")
    public Product getByid(int id) {return productService.get(id);}


}
