package application;
import framework.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/product")
public class NewController {
    @Autowired
    public ProductService productService;
    @GetMapping(path = "/")
    public String getProducts(){
        return productService.get();
    }

    @GetMapping(path = "/{id}")
    public Product getByid(int id) {return productService.get(id);}


}
