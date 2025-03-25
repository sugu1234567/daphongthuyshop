package vn.sugu.daphongthuyshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.sugu.daphongthuyshop.entity.ShippingAddress;
import vn.sugu.daphongthuyshop.entity.User;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, String> {
    List<ShippingAddress> findByUser(User user);

    Optional<ShippingAddress> findByIdAndUser(String id, User user);

    void deleteByIdAndUser(String id, User user);

    Optional<ShippingAddress> findByUserAndIsDefault(User user, boolean isDefault);
}
