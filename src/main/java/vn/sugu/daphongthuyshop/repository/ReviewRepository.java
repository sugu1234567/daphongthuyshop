package vn.sugu.daphongthuyshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.sugu.daphongthuyshop.entity.Product;
import vn.sugu.daphongthuyshop.entity.Review;
import vn.sugu.daphongthuyshop.entity.User;

public interface ReviewRepository extends JpaRepository<Review, String> {
    List<Review> findByProduct(Product product);

    Optional<Review> findByUserAndProduct(User user, Product product);
}
