package org.badgers.buyerservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Table(name = "product_category")
@Getter
@Setter
@NoArgsConstructor
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "description", length = 70, nullable = false)
    private String description;

    @Column(name = "category_code", length = 20, nullable = false)
    private String categoryCode;

    @Column(name = "active", nullable = false)
    @ColumnDefault(value = "true")
    private Boolean active = true;

    @ManyToMany(mappedBy = "productCategory")
    private List<Product> products;


}
