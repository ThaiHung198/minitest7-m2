package controller;

import model.Order;

import java.util.Comparator;


public class TotalPriceComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        // Xử lý null cho đối tượng Order trước
        if (o1 == null && o2 == null) return 0;
        if (o1 == null) return -1; // null nhỏ hơn non-null
        if (o2 == null) return 1;  // non-null lớn hơn null

        // So sánh giá trị double trả về từ calculateTotalPrice
        return Double.compare(o1.calculateTotalPrice(), o2.calculateTotalPrice());
    }
}