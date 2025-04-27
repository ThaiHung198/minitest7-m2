package controller; // Đặt trong controller theo yêu cầu hình ảnh

import model.Order; // Phải import Order từ model
import java.util.Comparator;

public class CustomerNameComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        String name1 = (o1 == null || o1.getCustomerName() == null) ? "" : o1.getCustomerName();
        String name2 = (o2 == null || o2.getCustomerName() == null) ? "" : o2.getCustomerName();
        return name1.compareToIgnoreCase(name2);
    }
}