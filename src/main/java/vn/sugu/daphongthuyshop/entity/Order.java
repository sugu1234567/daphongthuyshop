package vn.sugu.daphongthuyshop.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.sugu.daphongthuyshop.enums.OrderStatus;
import vn.sugu.daphongthuyshop.enums.PaymentStatus;

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

    BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    OrderStatus status; // PENDING, PROCESSING, COMPLETED, CANCELED

    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus; // PAID, UNPAID

    LocalDateTime createdAt;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    OrderDetail orderDetail;
}
