package vn.sugu.daphongthuyshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.sugu.daphongthuyshop.entity.Order;
import vn.sugu.daphongthuyshop.entity.User;
import vn.sugu.daphongthuyshop.enums.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser(User user);

    List<Order> findByUserAndStatus(User user, OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.status = :status AND DATE(o.createdAt) BETWEEN :startDate AND :endDate")
    List<Order> findByStatusAndDateBetween(
            @Param("status") OrderStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}