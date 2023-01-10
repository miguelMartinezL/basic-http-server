import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public List<String> findAll()
    {
        List<String> prods = new ArrayList<>();
        for(int i = 0; i < products.size(); i++){
           prods.add(String.valueOf(
                   products.get(i).getId()
                   + " " + products.get(i).getPname()
                   + " " + products.get(i).getBatchno()
                   + " " + products.get(i).getPrice()
                   + " " + products.get(i).getNoofproduct() + "\n"
           ));
        }
        return prods;
    }

    public void addOne(int id, String pname, String batchno, double price, int noofproduct)
    {
        products.add(new Product(id,pname, batchno,price,noofproduct));
    }

    public boolean deleteOne(int id)
    {
        boolean check = products.removeIf(product -> product.getId() == id);

        //return products.get(id).getId();
        return check;
    }

   // public Product update(Map<String, Object> parameters) {}
}
