package vn.sugu.daphongthuyshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import vn.sugu.daphongthuyshop.dto.request.orderRequest.RevenueReportRequest;
import vn.sugu.daphongthuyshop.dto.request.orderRequest.TopProductsRequest;
import vn.sugu.daphongthuyshop.dto.response.authResponse.APIResponse;
import vn.sugu.daphongthuyshop.dto.response.orderResponse.RevenueReportResponse;
import vn.sugu.daphongthuyshop.dto.response.orderResponse.TopProductResponse;
import vn.sugu.daphongthuyshop.service.ReportService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    ReportService reportService;

    @PostMapping("/revenue")
    public APIResponse<RevenueReportResponse> getRevenueReport(@Valid @RequestBody RevenueReportRequest request) {
        RevenueReportResponse response = reportService.calculateRevenue(request);
        return APIResponse.<RevenueReportResponse>builder()
                .message("Báo cáo doanh thu đã được tạo thành công")
                .data(response)
                .build();
    }

    @PostMapping("/products/top-selling")
    public APIResponse<List<TopProductResponse>> getTopSellingProducts(@Valid @RequestBody TopProductsRequest request) {
        List<TopProductResponse> topProducts = reportService.getTopSellingProducts(request);
        return APIResponse.<List<TopProductResponse>>builder()
                .message("Lấy danh sách sản phẩm bán chạy thành công")
                .data(topProducts)
                .build();
    }
}