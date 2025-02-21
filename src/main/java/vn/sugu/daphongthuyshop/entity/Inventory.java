package vn.sugu.daphongthuyshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "inventory")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String inventoryId;

    @OneToOne
    @JoinColumn(name = "product_id")
    Product product;

    int quantity;

    public void updateStock(int newQuantity) {
        this.quantity = newQuantity;
    }
}
