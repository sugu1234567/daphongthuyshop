package vn.sugu.daphongthuyshop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.sugu.daphongthuyshop.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

    Optional<User> findByFullName(String fullName);

    Optional<User> findByEmail(String email);

    Page<User> findByFullNameContainingOrEmailContainingOrPhoneContaining(
            String fullName, String email, String phone, Pageable pageable);

}
