package vn.sugu.daphongthuyshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.sugu.daphongthuyshop.dto.response.authResponse.APIResponse;
import vn.sugu.daphongthuyshop.dto.response.dashboardResponse.DashboardStatsResponse;
import vn.sugu.daphongthuyshop.dto.response.dashboardResponse.RecentSoldProductResponse;
import vn.sugu.daphongthuyshop.dto.response.dashboardResponse.TopSellingProductResponse;
import vn.sugu.daphongthuyshop.service.DashboardService;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        DashboardStatsResponse stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/products/top-selling")
    public APIResponse<List<TopSellingProductResponse>> getTopSellingProducts() {
        List<TopSellingProductResponse> topProducts = dashboardService.getTopSellingProductsThisMonth();
        return APIResponse.<List<TopSellingProductResponse>>builder()
                .message("Lấy danh sách sản phẩm bán chạy trong tháng thành công")
                .data(topProducts)
                .build();
    }

    @GetMapping("/products/recent-sold")
    public APIResponse<List<RecentSoldProductResponse>> getRecentSoldProducts() {
        List<RecentSoldProductResponse> recentProducts = dashboardService.getRecentSoldProductsThisMonth();
        return APIResponse.<List<RecentSoldProductResponse>>builder()
                .message("Lấy danh sách sản phẩm bán mới nhất trong tháng thành công")
                .data(recentProducts)
                .build();
    }
}