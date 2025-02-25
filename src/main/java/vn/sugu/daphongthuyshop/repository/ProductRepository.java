package vn.sugu.daphongthuyshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.sugu.daphongthuyshop.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    boolean existsByName(String name);

    Optional<Product> findById(String productId);

}
