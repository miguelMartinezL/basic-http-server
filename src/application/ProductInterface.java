package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ProductInterface {
    List get();

    Product get(int id);

    boolean add(Product newProd);

    boolean delete(int id);

    boolean update(int id, Product prod);

}
