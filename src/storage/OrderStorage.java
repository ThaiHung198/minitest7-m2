package storage;

import model.Order; // Import lớp Order
import java.util.List;

public interface OrderStorage {
    // Ném Exception thay vì StorageException để đơn giản hơn
    void saveOrders(List<Order> orders) throws Exception;
    List<Order> loadOrders() throws Exception;
}