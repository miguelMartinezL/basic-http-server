import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ProductInterface {
    List<String> findAll();

    void addOne(int id, String pname, String batchno, double price, int noofproduct);

    boolean deleteOne(int id);

   // Product update(Map<String, Object> parameters);
}
