package org.badgers.buyerservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;
import java.util.Objects;

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

    @ManyToMany(mappedBy = "productCategory", cascade =  CascadeType.PERSIST)
    private List<Product> products;

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;

        ProductCategory that = (ProductCategory) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}