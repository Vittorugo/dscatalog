package com.devsuperior.dscatalog.entities;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tb_products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String description;

    private Double price;

    @Column(columnDefinition = "TEXT")
    private String imgUrl;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant date;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant updatedAt;

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    @ManyToMany
    @JoinTable(
            name = "tb_products_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    public Set<Category> categories = new HashSet<>();

    public Product(Long id, String name, String description, Double price, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }

//    // Implementação do padrão Builder sem usar o Lombok
//    private Product(Builder builder) {
//        this.name = builder.name;
//        this.description = builder.description;
//        this.imgUrl = builder.imgUrl;
//        this.price = builder.price;
//        this.date = builder.date;
//    }

//    public static class Builder {
//        private String name;
//        private String description;
//        private Double price;
//        private String imgUrl;
//        private Instant date = Instant.now();
//
//        public Builder() {}
//
//        public Builder name(String name) {
//            this.name = name;
//            return this;
//        }
//
//        public Builder description(String description) {
//            this.description = description;
//            return this;
//        }
//
//        public Builder price(Double price) {
//            this.price = price;
//            return this;
//        }
//
//        public Builder imgUrl(String imgUrl) {
//            this.imgUrl = imgUrl;
//            return this;
//        }
//
//        public Builder date(Instant date) {
//            this.date = date;
//            return this;
//        }
//
//        public Product build() {
//            return new Product(this);
//        }
//    }

}
