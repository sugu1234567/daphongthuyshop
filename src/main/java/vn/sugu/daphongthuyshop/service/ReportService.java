package vn.sugu.daphongthuyshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import vn.sugu.daphongthuyshop.dto.request.orderRequest.RevenueReportRequest;
import vn.sugu.daphongthuyshop.dto.request.orderRequest.TopProductsRequest;
import vn.sugu.daphongthuyshop.dto.response.orderResponse.RevenueReportResponse;
import vn.sugu.daphongthuyshop.dto.response.orderResponse.TopProductResponse;
import vn.sugu.daphongthuyshop.entity.Order;
import vn.sugu.daphongthuyshop.entity.OrderDetail;
import vn.sugu.daphongthuyshop.entity.Product;
import vn.sugu.daphongthuyshop.enums.OrderStatus;
import vn.sugu.daphongthuyshop.exception.AppException;
import vn.sugu.daphongthuyshop.exception.ErrorCode;
import vn.sugu.daphongthuyshop.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ReportService {

    OrderRepository orderRepository;

    public RevenueReportResponse calculateRevenue(RevenueReportRequest request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        // Kiểm tra tính hợp lệ của ngày bắt đầu và ngày kết thúc
        if (startDate == null || endDate == null) {
            throw new AppException(ErrorCode.DATE_INVALID);
        }

        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.DATE_INVALID);
        }

        List<Order> completedOrders = orderRepository.findByStatusAndDateBetween(
                OrderStatus.COMPLETED, startDate, endDate);

        // Tính tổng doanh thu
        BigDecimal totalRevenue = completedOrders.stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tính số lượng đơn hàng
        int orderCount = completedOrders.size();

        // Tính giá trị đơn hàng trung bình
        BigDecimal averageOrderValue = orderCount > 0
                ? totalRevenue.divide(BigDecimal.valueOf(orderCount), 2, BigDecimal.ROUND_HALF_UP)
                : BigDecimal.ZERO;

        // Đếm tổng số sản phẩm đã bán
        int totalProductsSold = completedOrders.stream()
                .flatMap(order -> order.getOrderDetails().stream())
                .mapToInt(OrderDetail::getQuantity)
                .sum();

        return RevenueReportResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalRevenue(totalRevenue)
                .orderCount(orderCount)
                .averageOrderValue(averageOrderValue)
                .totalProductsSold(totalProductsSold)
                .build();
    }

    public List<TopProductResponse> getTopSellingProducts(TopProductsRequest request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        int limit = request.getLimit() > 0 ? request.getLimit() : 10; // Mặc định lấy top 10 nếu không chỉ định

        // Kiểm tra tính hợp lệ của ngày bắt đầu và ngày kết thúc
        if (startDate == null || endDate == null) {
            throw new AppException(ErrorCode.DATE_INVALID);
        }

        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.DATE_INVALID);
        }

        List<Order> completedOrders = orderRepository.findByStatusAndDateBetween(
                OrderStatus.COMPLETED, startDate, endDate);

        // Map để theo dõi số lượng bán của từng sản phẩm
        Map<Product, Integer> productQuantityMap = new HashMap<>();
        Map<Product, BigDecimal> productRevenueMap = new HashMap<>();

        // Thống kê số lượng bán và doanh thu của từng sản phẩm
        for (Order order : completedOrders) {
            for (OrderDetail detail : order.getOrderDetails()) {
                Product product = detail.getProduct();
                int quantity = detail.getQuantity();
                BigDecimal revenue = detail.getPrice().multiply(BigDecimal.valueOf(quantity));

                // Cập nhật số lượng
                productQuantityMap.put(product,
                        productQuantityMap.getOrDefault(product, 0) + quantity);

                // Cập nhật doanh thu
                productRevenueMap.put(product,
                        productRevenueMap.getOrDefault(product, BigDecimal.ZERO).add(revenue));
            }
        }

        // Chuyển đổi map thành danh sách và sắp xếp theo số lượng bán giảm dần
        List<TopProductResponse> topProducts = new ArrayList<>();

        productQuantityMap.forEach((product, quantity) -> {
            topProducts.add(TopProductResponse.builder()
                    .productId(product.getProductId())
                    .productName(product.getName())
                    .category(product.getCategory().getName())
                    .quantitySold(quantity)
                    .revenue(productRevenueMap.get(product))
                    .build());
        });

        // Sắp xếp theo số lượng bán giảm dần và giới hạn số lượng kết quả trả về
        return topProducts.stream()
                .sorted(Comparator.comparing(TopProductResponse::getQuantitySold).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}