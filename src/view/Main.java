package view;

import controller.OrderManager;
import model.*; // Import các lớp model
import storage.BinaryFileStorage;
import storage.OrderStorage;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String DATA_FILE = "orders.dat"; // Tên file dữ liệu
    private static Scanner scanner = new Scanner(System.in);
    private static OrderManager manager;

    public static void main(String[] args) {
        OrderStorage storage = new BinaryFileStorage(DATA_FILE);
        manager = new OrderManager(storage);

        int choice;
        do {
            displayMenu();
            choice = getUserChoice();
            handleChoice(choice);
        } while (choice != 0);
        System.out.println("Đang lưu dữ liệu trước khi thoát...");
        manager.saveOrders();
        System.out.println("Tạm biệt!");
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n--- MENU QUẢN LÝ ĐƠN HÀNG ---");
        System.out.println("1. Thêm đơn hàng mới");
        System.out.println("2. Xóa đơn hàng theo ID");
        System.out.println("3. Hiển thị tất cả đơn hàng");
        System.out.println("4. Hiển thị báo cáo doanh thu");
        System.out.println("5. Sắp xếp theo ngày đặt hàng");
        System.out.println("6. Sắp xếp theo tên khách hàng");
        System.out.println("7. Sắp xếp theo tổng giá");
        System.out.println("8. Lưu dữ liệu theo cách thủ công");
        System.out.println("0. Thoát");
        System.out.print("Nhập lựa chọn của bạn: ");
    }

    private static int getUserChoice() {
        int choice = -1;
        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Đầu vào không hợp lệ. Vui lòng nhập số.");
        } finally {
            scanner.nextLine();
        }
        return choice;
    }

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
                System.out.println("Đơn hàng được sắp xếp theo ngày. Hiển thị danh sách cập nhật:");
                displayAllOrders();
                break;
            case 6:
                manager.sortByCustomerName();
                System.out.println("Đơn hàng được sắp xếp theo tên. Hiển thị danh sách cập nhật:");
                displayAllOrders();
                break;
            case 7:
                manager.sortByTotalPrice();
                System.out.println("Đơn hàng được sắp xếp theo giá. Hiển thị danh sách cập nhật:");
                displayAllOrders();
                break;
            case 8:
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
    private static void addOrder() {
        System.out.println("\n--- Thêm đơn hàng mới ---");
        System.out.print("Nhập ID đơn hàng: ");
        String id = scanner.nextLine().trim();
        System.out.print("Nhập tên khách hàng: ");
        String name = scanner.nextLine().trim();
        int date = readInt("Nhập ngày đặt hàng (yyyyMMdd): ");
        if (date == -1) return; // Nhập lỗi

        System.out.print("Order Type (1: Điện tử, 2: Quần áo): ");
        int type = readInt("");
        if (type != 1 && type != 2) {
            System.out.println("Loại không hợp lệ.");
            return;
        }

        Order newOrder = null;
        if (type == 1) {
            double price = readDouble("Nhập giá mặt hàng: ");
            if (price == -1) return;
            int warranty = readInt("Nhập tháng bảo hành: ");
            if (warranty == -1) return;
            newOrder = new ElectronicsOrder(id, name, date, price, warranty);
        } else { // type == 2
            double price = readDouble("Nhập giá cơ sở: ");
            if (price == -1) return;
            System.out.print("Nhập kích thước: ");
            String size = scanner.nextLine().trim();
            newOrder = new ClothingOrder(id, name, date, size, price);
        }

        if (manager.addOrder(newOrder)) {
            // Manager đã in thông báo thành công/thất bại
        }
    }
    private static void removeOrder() {
        System.out.println("\n--- Xóa đơn hàng ---");
        System.out.print("Nhập ID đơn hàng để xóa: ");
        String id = scanner.nextLine().trim();
        manager.removeOrder(id); // Manager sẽ in kết quả
    }

    private static void displayAllOrders() {
        System.out.println("\n--- Tất cả đơn hàng ---");
        List<Order> orders = manager.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("Không có đơn hàng nào để hiển thị.");
            return;
        }
        int count = 1;
        for (Order order : orders) {
            System.out.println("\n--- Đơn hàng #" + count++ + " ---");
            order.displayInfo(); // Đa hình
        }
        System.out.println("\n--------------------");
    }

    private static void displayRevenueReport() {
        System.out.println("\n--- Báo cáo doanh thu ---");
        List<Order> orders = manager.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("Không có lệnh báo cáo.");
            return;
        }
        System.out.printf("%-15s | %-25s | %s%n", "Order ID", "Tên khách hàng", "Tổng giá");
        System.out.println("----------------|---------------------------|---------------");
        double totalRevenue = 0;
        for (Order order : orders) {
            double price = order.calculateTotalPrice();
            System.out.printf("%-15s | %-25s | %.2f%n", order.getOrderId(), order.getCustomerName(), price);
            totalRevenue += price;
        }
        System.out.println("----------------|---------------------------|---------------");
        System.out.printf("%-43s | %.2f%n", "TỔNG DOANH THU:", totalRevenue);
    }

    // --- Helper đọc input ---
    private static int readInt(String prompt) {
        System.out.print(prompt);
        int value = -1;
        try {
            value = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Đầu vào không hợp lệ. Vui lòng nhập số.");
        } finally {
            scanner.nextLine(); // Consume newline
        }
        return value;
    }
    private static double readDouble(String prompt) {
        System.out.print(prompt);
        double value = -1.0;
        try {
            value = scanner.nextDouble();
        } catch (InputMismatchException e) {
            System.out.println("Đầu vào không hợp lệ. Vui lòng nhập số.");
        } finally {
            scanner.nextLine(); // Consume newline
        }
        return value;
    }
}
