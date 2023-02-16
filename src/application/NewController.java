package application;
import framework.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/product")
public class NewController {
    @Autowired
    public ProductService productService;
    @GetMapping(path = "/")
    public List getProducts(){
        return productService.get();
    }

    @GetMapping(path = "/{id}")
    public Product getByid(@PathVariable() Integer id) {return productService.get(id);}

    @PostMapping(path = "/")
    public boolean createProduct(Product prod) {return productService.add(prod);}


}
