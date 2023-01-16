import java.util.ArrayList;

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

    public String findAll()
    {
        String prods = "{";
        try {
            int i = products.size();
            for ( Product prod : products)
            {
                prods += Json.parseJson(prod) + (i == 1 ? "":",");
                i--;
            }
            prods += "}";
            return prods;
        } catch (Exception e) {
            return "";
        }
    }
    public String findById(int id){
        try {
            Product prod = products.stream().filter(product -> product.getId() == id).findFirst().get();
            return Json.parseJson(prod);
        } catch (Exception e) {
            return "";
        }
    }

    public String addOne(String json)
    {
        String response;
        try{
            Product newProd = Json.parseString(json, Product.class);
            response = findById(newProd.getId());
            if (response.isEmpty())
            {
                products.add(newProd);
                response = findById(newProd.getId());
            } else
            {
                response = "";
            }
        } catch (Exception e ){
            return "Error: parsing error";
        }
        return response;
    }

    public Boolean deleteAll()
    {
        return products.removeAll(products);
    }
    public Boolean deleteOne(int id)
    {
        return products.removeIf(product -> product.getId() == id);
    }

    public String updateOne(int id, String obj)
    {
        try{
            Product updated = Json.parseString(obj, Product.class);
            int index = products.indexOf(products.stream().filter(product -> product.getId() == id).findFirst().get());
            products.set(index, updated);
            return findById(id);
        } catch (Exception e){
            return e.toString();
        }
    }
}
