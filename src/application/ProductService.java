package application;

import framework.Json;
import framework.annotation.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements ProductInterface
{
    private ArrayList<Product> products = new ArrayList<Product>();
    public ProductService() {
        products.add(new Product(100, "Mobile", "CLK98123", 9000.00, 6));
        products.add(new Product(101, "Smart TV", "LGST09167", 60000.00, 3));
        products.add(new Product(102, "Washing Machine", "38753BK9", 9000.00, 7));
        products.add(new Product(103, "Laptop", "LHP29OCP", 24000.00, 1));
        products.add(new Product(104, "Air Conditioner", "ACLG66721", 30000.00, 5));
        products.add(new Product(105, "Refrigerator ", "12WP9087", 10000.00, 4));
    }

    public Product get()
    {
        //return products;
        return products.get(0);
    }
    public Product get(int id){
            return products.contains(products.stream().filter(product -> product.getId() == id).findFirst().get()) ?
                    products.stream().filter(product -> product.getId() == id).findFirst().get() : null;

    }

    public boolean add(Product newProd)
    {
        return products.contains(newProd) ? false : products.add(newProd);
    }

    public boolean delete(int id)
    {
        return products.removeIf(product -> product.getId() == id);
    }

    public boolean update(int id, Product prod)
    {
            int index = products.indexOf(products.stream().filter(product -> product.getId() == id).findFirst().get());
            products.set(index, prod);
            return products.contains(prod);
    }
}
