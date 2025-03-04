package vn.sugu.daphongthuyshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.sugu.daphongthuyshop.entity.Cart;
import vn.sugu.daphongthuyshop.entity.User;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser(User user);
}