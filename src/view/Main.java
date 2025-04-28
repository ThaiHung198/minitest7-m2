package view; // Đặt trong package view

import controller.OrderManager;
import model.*; // Import các lớp model
import storage.OrderStorage; // Import lớp OrderStorage (class)

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String DATA_FILE = "orders.dat"; // Tên file dữ liệu (thường không dịch)
    private static Scanner scanner = new Scanner(System.in); // Scanner cho toàn bộ lớp
    private static OrderManager manager; // Đối tượng quản lý

    public static void main(String[] args) {
        // Khởi tạo Storage và Manager
        OrderStorage storage = new OrderStorage(DATA_FILE);
        manager = new OrderManager(storage); // Manager sẽ tự load dữ liệu

        int choice;
        do {
            displayMenu();
            choice = getUserChoice();
            handleChoice(choice);
        } while (choice != 0);

        // Lưu dữ liệu trước khi thoát
        System.out.println("Đang lưu dữ liệu trước khi thoát...");
        manager.saveOrders();
        System.out.println("Tạm biệt!");
        scanner.close(); // Đóng scanner
    }

    // Hiển thị menu
    private static void displayMenu() {
        System.out.println("\n--- MENU QUẢN LÝ ĐƠN HÀNG ---");
        System.out.println("1. Thêm đơn hàng mới");
        System.out.println("2. Xóa đơn hàng theo mã");
        System.out.println("3. Hiển thị tất cả đơn hàng");
        System.out.println("4. Hiển thị báo cáo doanh thu");
        System.out.println("5. Sắp xếp theo ngày đặt hàng");
        System.out.println("6. Sắp xếp theo tên khách hàng");
        System.out.println("7. Sắp xếp theo tổng tiền");
        System.out.println("8. Lưu dữ liệu thủ công");
        System.out.println("0. Thoát");
        System.out.print("Nhập lựa chọn của bạn: ");
    }

    // Lấy lựa chọn từ người dùng
    private static int getUserChoice() {
        int choice = -1;
        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Đầu vào không hợp lệ. Vui lòng nhập một số.");
        } finally {
            scanner.nextLine(); // Luôn đọc dòng mới sau nextInt()
        }
        return choice;
    }

    // Xử lý lựa chọn
    private static void handleChoice(int choice) {
        switch (choice) {
            case 1:
                addOrder();
                break;
            case 2:
                removeOrder();
                break;
            case 3:
                displayAllOrders();
                break;
            case 4:
                displayRevenueReport();
                break;
            case 5:
                manager.sortByOrderDate();
                System.out.println("Đã sắp xếp theo ngày. Hiển thị danh sách cập nhật:");
                displayAllOrders();
                break;
            case 6:
                manager.sortByCustomerName();
                System.out.println("Đã sắp xếp theo tên. Hiển thị danh sách cập nhật:");
                displayAllOrders();
                break;
            case 7:
                manager.sortByTotalPrice();
                System.out.println("Đã sắp xếp theo giá. Hiển thị danh sách cập nhật:");
                displayAllOrders();
                break;
            case 8:
                System.out.println("Đang lưu dữ liệu thủ công...");
                manager.saveOrders(); // Lưu thủ công
                break;
            case 0:
                // Xử lý thoát đã thực hiện trong vòng lặp main
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
        }
        // Tạm dừng sau mỗi hành động (trừ khi thoát)
        if (choice != 0) {
            System.out.print("\nNhấn Enter để tiếp tục...");
            scanner.nextLine();
        }
    }

    // === Các hàm chức năng gọi Manager và hiển thị ===

    private static void addOrder() {
        System.out.println("\n--- Thêm Đơn Hàng Mới ---");
        String id = readString("Nhập mã đơn hàng: ");
        if (id.isEmpty()) { System.out.println("Mã đơn hàng không được để trống."); return; }

        String name = readString("Nhập tên khách hàng: ");
        if (name.isEmpty()) { System.out.println("Tên khách hàng không được để trống."); return; } // Giả sử tên bắt buộc

        int date = readInt("Nhập ngày đặt hàng (yyyyMMdd): ");
        if (date == -1) return; // Nhập lỗi

        System.out.print("Loại đơn hàng (1: Điện tử, 2: Quần áo): ");
        int type = readInt("");
        if (type != 1 && type != 2) {
            System.out.println("Loại không hợp lệ.");
            return;
        }

        Order newOrder = null;
        if (type == 1) {
            double price = readDouble("Nhập giá sản phẩm: ");
            if (price < 0) { System.out.println("Giá không được âm."); return; }
            int warranty = readInt("Nhập số tháng bảo hành (>=0): ");
            if (warranty < 0) { System.out.println("Bảo hành không được âm."); return; }
            newOrder = new ElectronicsOrder(id, name, date, price, warranty);
        } else { // type == 2
            double price = readDouble("Nhập giá gốc: ");
            if (price < 0) { System.out.println("Giá không được âm."); return; }
            String size = readString("Nhập kích thước: ");
            if (size.isEmpty()) { System.out.println("Kích thước không được để trống."); return; }
            newOrder = new ClothingOrder(id, name, date, size, price);
        }

        if (manager.addOrder(newOrder)) {
            System.out.println("Thêm đơn hàng thành công!");
        } else {
            // Lỗi đã được OrderManager in ra (ví dụ: ID trùng lặp)
            System.out.println("Thêm đơn hàng thất bại."); // Thêm thông báo chung nếu manager không in
        }
    }

    private static void removeOrder() {
        System.out.println("\n--- Xóa Đơn Hàng ---");
        String id = readString("Nhập mã đơn hàng cần xóa: ");
        if (id.isEmpty()) { System.out.println("Mã đơn hàng không được để trống."); return; }
        if (manager.removeOrder(id)) {
            System.out.println("Xóa đơn hàng thành công!");
        } else {
            System.out.println("Không tìm thấy đơn hàng hoặc xóa thất bại."); // Manager có thể đã in lỗi not found
        }
    }

    private static void displayAllOrders() {
        System.out.println("\n--- Tất Cả Đơn Hàng ---");
        List<Order> orders = manager.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("Không có đơn hàng nào để hiển thị.");
            return;
        }
        int count = 1;
        for (Order order : orders) {
            System.out.println("\n--- Đơn hàng #" + count++ + " ---"); // Chú ý nối chuỗi
            order.displayInfo(); // Đa hình
        }
        System.out.println("\n--------------------");
    }

    private static void displayRevenueReport() {
        System.out.println("\n--- Báo Cáo Doanh Thu ---");
        List<Order> orders = manager.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("Không có đơn hàng cho báo cáo.");
            return;
        }
        // Sử dụng tiêu đề tiếng Việt
        System.out.printf("%-15s | %-25s | %s%n", "Mã ĐH", "Tên Khách Hàng", "Tổng Tiền");
        System.out.println("----------------|---------------------------|---------------");
        double totalRevenue = 0;
        for (Order order : orders) {
            double price = order.calculateTotalPrice();
            System.out.printf("%-15s | %-25s | %.2f%n", order.getOrderId(), order.getCustomerName(), price);
            totalRevenue += price;
        }
        System.out.println("----------------|---------------------------|---------------");
        // Sử dụng tiêu đề tiếng Việt
        System.out.printf("%-43s | %.2f%n", "TỔNG DOANH THU:", totalRevenue);
    }

    // --- Helper đọc input ---
    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        int value = -1; // Giá trị trả về nếu lỗi
        try {
            value = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Đầu vào không hợp lệ. Vui lòng nhập số nguyên.");
        } finally {
            scanner.nextLine(); // Consume newline
        }
        return value;
    }
    private static double readDouble(String prompt) {
        System.out.print(prompt);
        double value = -1.0; // Giá trị trả về nếu lỗi
        try {
            value = scanner.nextDouble();
        } catch (InputMismatchException e) {
            System.out.println("Đầu vào không hợp lệ. Vui lòng nhập một số.");
        } finally {
            scanner.nextLine(); // Consume newline
        }
        return value;
    }
}