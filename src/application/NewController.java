package application;
import framework.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/product")
public class NewController {
    // agregar anotacion autowired
    @Autowired
    ProductService productService ;
    @GetMapping(path = "/")
    public List getProducts(){
        return productService.get();
    }

    @GetMapping(path = "/{id}")
    public Product getByid(int id) {return productService.get(id);}


}
