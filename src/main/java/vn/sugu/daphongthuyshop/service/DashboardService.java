package vn.sugu.daphongthuyshop.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.sugu.daphongthuyshop.dto.response.dashboardResponse.DashboardStatsResponse;
import vn.sugu.daphongthuyshop.dto.response.dashboardResponse.RecentSoldProductResponse;
import vn.sugu.daphongthuyshop.dto.response.dashboardResponse.TopSellingProductResponse;
import vn.sugu.daphongthuyshop.enums.OrderStatus;
import vn.sugu.daphongthuyshop.repository.OrderDetailRepository;
import vn.sugu.daphongthuyshop.repository.OrderRepository;

@Service
public class DashboardService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public DashboardStatsResponse getDashboardStats() {
        return DashboardStatsResponse.builder()
                .totalOrders(getTotalOrders())
                .currentMonthRevenue(getCurrentMonthRevenue())
                .pendingOrders(getPendingOrdersCount())
                .processingOrders(getProcessingOrdersCount())
                .build();
    }

    public long getTotalOrders() {
        return orderRepository.count();
    }

    public BigDecimal getCurrentMonthRevenue() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        BigDecimal revenue = orderRepository.calculateRevenueByDateRangeAndStatus(
                startOfMonth, endOfMonth, OrderStatus.COMPLETED);

        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    public long getPendingOrdersCount() {
        return orderRepository.countByStatus(OrderStatus.PENDING);
    }

    public long getProcessingOrdersCount() {
        return orderRepository.countByStatus(OrderStatus.PROCESSING);
    }

    public List<TopSellingProductResponse> getTopSellingProductsThisMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Object[]> results = orderDetailRepository.findTopSellingProducts(
                startOfMonth, endOfMonth);

        return results.stream()
                .limit(10)
                .map(row -> TopSellingProductResponse.builder()
                        .productId((String) row[0])
                        .productName((String) row[1])
                        .imageUrl((String) row[2])
                        .price((BigDecimal) row[3])
                        .totalSold((Long) row[4])
                        .totalRevenue((BigDecimal) row[5])
                        .build())
                .collect(Collectors.toList());
    }

    public List<RecentSoldProductResponse> getRecentSoldProductsThisMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Object[]> results = orderDetailRepository.findRecentSoldProducts(
                startOfMonth, endOfMonth);

        return results.stream()
                .limit(10)
                .map(row -> RecentSoldProductResponse.builder()
                        .productId((String) row[0])
                        .productName((String) row[1])
                        .imageUrl((String) row[2])
                        .price((BigDecimal) row[3])
                        .quantitySold((Integer) row[4])
                        .soldAt((LocalDateTime) row[5])
                        .orderNumber(row[6] != null ? row[6].toString() : "")
                        .build())
                .collect(Collectors.toList());
    }

    public BigDecimal getRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal revenue = orderRepository.calculateRevenueByDateRangeAndStatus(
                startDate, endDate, OrderStatus.COMPLETED);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    public long getOrderCountByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }
}