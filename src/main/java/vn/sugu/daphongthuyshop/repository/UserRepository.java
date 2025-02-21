package vn.sugu.daphongthuyshop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.sugu.daphongthuyshop.entity.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

    Optional<User> findByFullName(String fullName);

    Optional<User> findByEmail(String email);

    Page<User> findByFullNameContainingOrEmailContainingOrPhoneContaining(
            String fullName, String email, String phone, Pageable pageable);
}
