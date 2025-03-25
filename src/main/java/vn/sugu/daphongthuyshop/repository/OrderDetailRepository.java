package vn.sugu.daphongthuyshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.sugu.daphongthuyshop.entity.Order;
import vn.sugu.daphongthuyshop.entity.OrderDetail;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    List<OrderDetail> findByOrder(Order order);
}