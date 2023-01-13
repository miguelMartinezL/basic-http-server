import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ProductInterface {
    String findAll();

    String findById(int id);

    String addOne(String json);

    Boolean deleteAll();

    Boolean deleteOne(int id);

   // Product update(Map<String, Object> parameters);
}
