package controller;

import model.Order;
import storage.OrderStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OrderManager {

    private List<Order> orders;
    private final OrderStorage storage; // Giờ đây là tham chiếu đến lớp OrderStorage

    // Constructor nhận vào đối tượng OrderStorage (class)
    public OrderManager(OrderStorage storage) {
        this.storage = Objects.requireNonNull(storage, "Storage không được là null"); // Dịch
        loadOrders();
    }

    private void loadOrders() {
        this.orders = storage.loadOrders(); // Gọi trực tiếp, loadOrders trả về list rỗng nếu lỗi
        if (this.orders == null) {
            System.err.println("CẢNH BÁO QUAN TRỌNG: loadOrders() trả về null. Buộc khởi tạo danh sách rỗng."); // Dịch
            this.orders = new ArrayList<>();
        }

        // Thêm dữ liệu mẫu nếu danh sách rỗng sau khi load
        boolean hadSampleData = false; // Biến để kiểm tra xem có thêm mẫu không
        if (this.orders.isEmpty()) {
            System.out.println("THÔNG TIN: Không tìm thấy hoặc không tải được dữ liệu hiện có. Khởi tạo với các đơn hàng mẫu."); // Dịch
            initializeSampleData();
            hadSampleData = true;
        }
        // In thông báo tổng kết sau cùng
        System.out.println("THÔNG TIN: OrderManager đã khởi tạo. " + (hadSampleData ? "" : "Đã tải ") + this.orders.size() + " đơn hàng" + (hadSampleData ? " (mẫu)." : ".")); // Dịch và điều chỉnh log

    }

    private void initializeSampleData() {
        try {
            this.orders.add(new model.ElectronicsOrder("ELEC001", "Nguyen Van A", 20231027, 15500000.0, 24));
            this.orders.add(new model.ClothingOrder("CLOTH002", "Tran Thi B", 20231026, "L", 450000.0)); // Giá trước, Size sau
            this.orders.add(new model.ClothingOrder("CLOTH003", "Le Van C", 20231028, "M", 250000.0)); // Giá trước, Size sau
            System.out.println("THÔNG TIN: Đã thêm các đơn hàng mẫu.");
        } catch (Exception e) {
            System.err.println("LỖI tạo dữ liệu mẫu: " + e.getMessage());
        }
    }


    public void saveOrders() {
        storage.saveOrders(this.orders);
    }

    public boolean addOrder(Order order) {
        if (order == null || order.getOrderId() == null || order.getOrderId().trim().isEmpty()) {
            System.err.println("LỖI: Dữ liệu đơn hàng không hợp lệ (ID null hoặc rỗng)."); // Dịch
            return false;
        }
        String newId = order.getOrderId().trim();
        for (Order existing : orders) {
            if (newId.equals(existing.getOrderId())) {
                System.err.println("LỖI: Mã đơn hàng '" + newId + "' đã tồn tại."); // Dịch
                return false;
            }
        }
        orders.add(order);
        return true;
    }

    public boolean removeOrder(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            System.err.println("LỖI: Mã đơn hàng cần xóa không được để trống.");
            return false;
        }
        String idToRemove = orderId.trim();
        boolean removed = orders.removeIf(order -> idToRemove.equals(order.getOrderId()));
        return removed;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public void sortByOrderDate() {
        if (orders.size() > 1) {
            Collections.sort(orders);
        }
    }

    public void sortByCustomerName() {
        if (orders.size() > 1) {
            orders.sort(new CustomerNameComparator());
        }
    }

    public void sortByTotalPrice() {
        if (orders.size() > 1) {
            orders.sort(new TotalPriceComparator());
        }
    }
}