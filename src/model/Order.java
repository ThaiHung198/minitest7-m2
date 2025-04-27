package model;

import java.io.Serializable;
import java.util.Objects;

public abstract class Order implements Payable, Comparable<Order>, Serializable {
    private static final long serialVersionUID = 1L;
    private String orderId;
    private String customerName;
    private int orderDate;

    public Order() {
    }

    public Order(String orderId, String customerName, int orderDate) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.orderDate = orderDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(int orderDate) {
        this.orderDate = orderDate;
    }

    public abstract void displayInfo();

    @Override
    public abstract double calculateTotalPrice();

    @Override
    public int compareTo(Order o) {
        return Integer.compare(this.orderDate, o.orderDate);
    }

    // equals và hashCode dựa trên orderId ( để tránh trùng lặp)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(getOrderId(), order.getOrderId()); // So sánh ID đã xử lý null
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId()); // Hash code dựa trên ID đã xử lý null
    }

    @Override
    public String toString() { // Dùng cho debug hoặc log cơ bản
        return String.format("ID: %s, Name: %s, Date: %d", orderId, customerName, orderDate);
    }
}
