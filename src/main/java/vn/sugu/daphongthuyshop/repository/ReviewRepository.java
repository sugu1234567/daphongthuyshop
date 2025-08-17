package vn.sugu.daphongthuyshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.sugu.daphongthuyshop.entity.Product;
import vn.sugu.daphongthuyshop.entity.Review;
import vn.sugu.daphongthuyshop.entity.User;

public interface ReviewRepository extends JpaRepository<Review, String> {
    List<Review> findByProduct(Product product);

    Optional<Review> findByUserAndProduct(User user, Product product);

    boolean existsByOrderDetailId(String orderDetailId);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.product = :product")
    Double findAverageRatingByProduct(@Param("product") Product product);

    Integer countByProduct(Product product);
}
