package vn.sugu.daphongthuyshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.sugu.daphongthuyshop.entity.Order;
import vn.sugu.daphongthuyshop.entity.OrderDetail;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
        List<OrderDetail> findByOrder(Order order);

        @Query("SELECT od.product.productId, " +
                        "od.product.name, " +
                        "(SELECT pi.imageUrl FROM ProductImage pi WHERE pi.product = od.product ORDER BY pi.id LIMIT 1), "
                        +
                        "od.product.price, " +
                        "SUM(od.quantity) as totalSold, " +
                        "SUM(od.quantity * od.price) as totalRevenue " +
                        "FROM OrderDetail od JOIN od.order o " +
                        "WHERE o.status = 'COMPLETED' " +
                        "AND o.createdAt >= :startDate " +
                        "AND o.createdAt <= :endDate " +
                        "GROUP BY od.product.productId, od.product.name, od.product.price " +
                        "ORDER BY SUM(od.quantity) DESC")
        List<Object[]> findTopSellingProducts(
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        @Query("SELECT od.product.productId, " +
                        "od.product.name, " +
                        "(SELECT pi.imageUrl FROM ProductImage pi WHERE pi.product = od.product ORDER BY pi.id LIMIT 1), "
                        +
                        "od.product.price, " +
                        "od.quantity, " +
                        "o.createdAt, " +
                        "o.orderId " +
                        "FROM OrderDetail od " +
                        "JOIN od.order o " +
                        "WHERE o.status = 'COMPLETED' " +
                        "AND o.createdAt >= :startDate " +
                        "AND o.createdAt <= :endDate " +
                        "ORDER BY o.createdAt DESC")
        List<Object[]> findRecentSoldProducts(
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);
}