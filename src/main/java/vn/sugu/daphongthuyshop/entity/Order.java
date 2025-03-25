package vn.sugu.daphongthuyshop.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.sugu.daphongthuyshop.enums.OrderStatus;

@Entity
@Table(name = "orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    String shippingAddress;

    @Column(columnDefinition = "MEDIUMTEXT")
    String note;

    BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    OrderStatus status; // PENDING, PROCESSING, COMPLETED, CANCELED

    LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    List<OrderDetail> orderDetails = new ArrayList<>();

    String paymentMethod;
}
