package vn.sugu.daphongthuyshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.sugu.daphongthuyshop.entity.Order;
import vn.sugu.daphongthuyshop.entity.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser(User user);
}