package model;

public class ClothingOrder extends Order {
    private static final long serialVersionUID = 3L;
    private double basePrice;
    private String size;
    private static final double LARGE_SIZE_SURCHARGE_RATE = 0.10; // 10% phụ phí


    public ClothingOrder(String size, double basePrice) {
        this.size = size;
        this.basePrice = basePrice;
    }

    public ClothingOrder(String orderId, String customerName, int oderDate, String size, double basePrice) {
        super(orderId, customerName, oderDate);
        this.basePrice = basePrice;
        this.size = (size == null || size.trim().isEmpty()) ? "N/A" : size.toUpperCase(); // Chuẩn hóa size

    }

    public double getBasePrice() {
        return basePrice;
    }


    public String getSize() {
        return size;
    }


    @Override
    public double calculateTotalPrice() {
        if ("L".equals(size) || "XL".equals(size)) { // So sánh với size đã chuẩn hóa
            return this.basePrice * (1 + LARGE_SIZE_SURCHARGE_RATE);
        } else {
            return this.basePrice;
        }
    }

    @Override
    public void displayInfo() {
        System.out.println("--- Clothing Order ---");
        System.out.println("ID đơn hàng     : " + getOrderId());
        System.out.println("Tên khách hàng  : " + getCustomerName());
        System.out.println("Ngày đặt hàng   : " + getOrderDate());
        System.out.println("Giá cơ sở       : " + String.format("%.2f", basePrice));
        System.out.println("Kích thước      : " + size);
        // Hiển thị phụ phí nếu có
        if (size.equalsIgnoreCase("L") || size.equalsIgnoreCase("XL")) {
            System.out.println("Phụ phí kích thước : " + String.format("%.2f", basePrice * LARGE_SIZE_SURCHARGE_RATE));
        }
        System.out.println("Tổng giá    : " + String.format("%.2f", calculateTotalPrice()));
    }

    @Override
    public String toString() {
        return "[Clothing] " + super.toString();
    }
}

