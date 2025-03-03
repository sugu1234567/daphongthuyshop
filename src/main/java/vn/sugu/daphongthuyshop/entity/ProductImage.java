package vn.sugu.daphongthuyshop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "product_images")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;
}