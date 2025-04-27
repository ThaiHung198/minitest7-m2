package controller;

import model.*;
import storage.OrderStorage; // Import interface Storage

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class OrderManager {

    private List<Order> orders; // Danh sách chính chứa các đơn hàng
    private final OrderStorage storage; // Đối tượng thực hiện việc lưu/tải


    public OrderManager(OrderStorage storage) {
        this.storage = Objects.requireNonNull(storage, "Việc triển khai lưu trữ không thể là null");
        // Bước 1: Cố gắng tải dữ liệu
        loadOrders();

        // Bước 2: Kiểm tra xem danh sách có thực sự rỗng hay không *SAU KHI* tải
        if (this.orders == null || this.orders.isEmpty()) {
            System.out.println("THÔNG TIN: Không có dữ liệu hiện có nào được tải hoặc tìm thấy. Đang khởi tạo với các đơn hàng mẫu.");
            // Bước 3: Khởi tạo danh sách với dữ liệu mẫu
            initializeSampleData();
        } else {
            // Nếu tải thành công và có dữ liệu
            System.out.println("THÔNG TIN: Đã tải thành công " + this.orders.size() + " đơn đặt hàng hiện có.");
        }
    }

    private void loadOrders() {
        try {
            // Gọi phương thức load của đối tượng storage được truyền vào
            this.orders = storage.loadOrders();

            //  Xử lý trường hợp storage trả về null
            if (this.orders == null) {
                System.err.println("CẢNH BÁO: Bộ nhớ trả về một danh sách rỗng. Khởi tạo một danh sách rỗng.");
                this.orders = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("LỖI khi tải đơn hàng từ kho lưu trữ: " + e.getMessage());
            System.err.println("THÔNG TIN: Khởi tạo với danh sách đơn hàng trống do lỗi tải.");
            this.orders = new ArrayList<>();
        }
    }


    private void initializeSampleData() {
        // Luôn tạo mới list để đảm bảo không thêm vào list cũ (nếu có lỗi logic trước đó)
        this.orders = new ArrayList<>();
        try {
            // Thêm các đơn hàng mẫu trực tiếp vào danh sách
            this.orders.add(new ElectronicsOrder("ELEC001", "Nguyen Van An", 20231027, 15500000.0, 24));
            this.orders.add(new ClothingOrder("CLOTH002", "Tran Thi Binh", 20231026, "L", 450000.0));
            this.orders.add(new ClothingOrder("CLOTH003", "Le Van Cuong", 20231028, "M", 250000.0));
            this.orders.add(new ElectronicsOrder("ELEC004", "Pham Thi Dung", 20231029, 2100000.0, 12));
            this.orders.add(new ClothingOrder("CLOTH005", "Hoang Van Em", 20231025, "XL", 300000.0)); // Thêm size XL

            System.out.println("THÔNG TIN: Danh sách được khởi tạo với " + this.orders.size() + " đơn đặt hàng mẫu.");

        } catch (Exception e) {
            System.err.println("LỖI : Không thể khởi tạo dữ liệu mẫu: " + e.getMessage());
            // Đảm bảo orders vẫn là list rỗng để tránh NullPointerException
            this.orders = new ArrayList<>();
        }
    }

    /**
     * Lưu danh sách đơn hàng hiện tại vào storage.
     */
    public void saveOrders() {
        try {
            // Gọi phương thức save của đối tượng storage
            storage.saveOrders(this.orders);
        } catch (Exception e) { // Bắt lỗi rộng
            System.err.println("LỖI khi lưu đơn hàng vào kho: " + e.getMessage());
            throw new RuntimeException("Không lưu được đơn hàng. Vui lòng kiểm tra kho lưu trữ.", e);
        }
    }
    public boolean addOrder(Order order) {
        // Kiểm tra đầu vào cơ bản
        if (order == null || order.getOrderId() == null || order.getOrderId().trim().isEmpty()) {
            System.err.println("LỖI (Quản lý): Không thể thêm đơn hàng có ID rỗng hoặc rỗng.");
            return false;
        }
        String newId = order.getOrderId().trim();

        // Kiểm tra ID trùng lặp
        for (Order existing : orders) {
            if (newId.equals(existing.getOrderId())) {
                System.err.println("LỖI (Quản lý): Mã đơn hàng'" + newId + "' đã tồn tại.");
                return false;
            }
        }
        orders.add(order);
        return true;
    }

    public boolean removeOrder(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            System.err.println("LỖI (Quản lý): Mã đơn hàng cần xóa không được để trống.");
            return false;
        }
        String idToRemove = orderId.trim();
        // Dùng removeIf cho ngắn gọn (Java 8+)
        boolean removed = orders.removeIf(order -> idToRemove.equals(order.getOrderId()));
        return removed; // Trả về kết quả để Main/View thông báo
    }

    public List<Order> getAllOrders() {
        // Luôn trả về một bản sao để bảo vệ danh sách nội bộ
        return new ArrayList<>(this.orders);
    }

    public void sortByOrderDate() {
        if (orders.size() > 1) {
            Collections.sort(orders); // Sắp xếp trực tiếp danh sách nội bộ
        }
    }

    public void sortByCustomerName() {
        if (orders.size() > 1) {
            orders.sort(new CustomerNameComparator()); // Sắp xếp trực tiếp
        }
    }

    public void sortByTotalPrice() {
        if (orders.size() > 1) {
            orders.sort(new TotalPriceComparator()); // Sắp xếp trực tiếp
        }
    }
}