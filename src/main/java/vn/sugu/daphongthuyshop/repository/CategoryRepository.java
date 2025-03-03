package vn.sugu.daphongthuyshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.sugu.daphongthuyshop.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

    Optional<Category> findByName(String name);

    Optional<Category> findById(String categoryId);

    Page<Category> findByNameContaining(String name, Pageable pageable);

    List<Category> findByIsDeletedFalse();
}
