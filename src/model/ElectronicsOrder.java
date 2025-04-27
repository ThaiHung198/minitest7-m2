package model;

public class ElectronicsOrder extends Order {
    private static final long serialVersionUID = 2L;
    private double itemPrice;
    private int warrantyMonths;
    private static final double WARRANTY_COST_PER_MONTH = 50.0;

    public ElectronicsOrder(double itemPrice, int warrantyMonths) {
        this.itemPrice = itemPrice;
        this.warrantyMonths = warrantyMonths;
    }

    public ElectronicsOrder(String orderId, String customerName, int oderDate, double itemPrice, int warrantyMonths) {
        super(orderId, customerName, oderDate);
        this.itemPrice = itemPrice;
        this.warrantyMonths = warrantyMonths;
    }

    public double getItemPrice() {
        return itemPrice;
    }


    public int getWarrantyMonths() {
        return warrantyMonths;
    }


    @Override
    public double calculateTotalPrice() {
        return itemPrice + (warrantyMonths * WARRANTY_COST_PER_MONTH);
    }

    @Override
    public void displayInfo() {
        System.out.println("--- Đặt hàng điện tử ---");
        System.out.println("ID đơn hàng       : " + getOrderId());
        System.out.println("Tên khách hàng  : " + getCustomerName());
        System.out.println("Ngày đặt hàng     : " + getOrderDate());
        System.out.println("Giá mặt hàng     : " + String.format("%.2f", itemPrice));
        System.out.println("Tháng bảo hành: " + warrantyMonths);
        System.out.println("Chi phí bảo hành  : " + String.format("%.2f", warrantyMonths * WARRANTY_COST_PER_MONTH));
        System.out.println("Tổng giá    : " + String.format("%.2f", calculateTotalPrice())); // Gọi phương thức tính tổng tiền
    }

    @Override
    public String toString() {
        return "[Electronics] " + super.toString();
    }
}

