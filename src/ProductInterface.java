import java.util.ArrayList;
import java.util.List;
public interface ProductInterface {
    List<String> findAll();

    void addOne(int id, String pname, String batchno, double price, int noofproduct);
}
