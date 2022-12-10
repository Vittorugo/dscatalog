package com.devsuperior.dscatalog.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_products")
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

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant date;

    private Product(String name, String description, String imgUrl) {
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
    }

    public static Product withNameDescriptionImg(String name, String description, String imgUrl) {
        return new Product(name, description, imgUrl);
    }
}
