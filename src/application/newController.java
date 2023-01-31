package application;
import framework.annotation.GetMapping;


public class newController {
    private ProductInterface productInterface;
    @GetMapping(value = "/products")
    public String getProducts(){
        return productInterface.findAll();
    }


}
