package vn.sugu.daphongthuyshop.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.sugu.daphongthuyshop.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    boolean existsByName(String name);

    Optional<Product> findById(String productId);

    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR LOWER(p.name) LIKE %:name%) AND " +
            "(:categoryName IS NULL OR LOWER(p.category.name) LIKE %:categoryName%) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND p.isDeleted = false")
    List<Product> findByCriteria(
            @Param("name") String name,
            @Param("categoryName") String categoryName,
            @Param("minPrice") BigDecimal minPrice,
            Pageable pageable);
}
