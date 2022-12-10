package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private Double price;
    private String description;
    private String imgUrl;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.imgUrl = product.getImgUrl();
    }

//    public static ProductDto withNamePrice(String name,Double price) {
//        return new ProductDto(name, price);
//    }
//
//    public static ProductDto withNamePriceDescription(String name, Double price, String description) {
//        return new ProductDto(name, price, description);
//    }
//
//    public static ProductDto withNamePriceDescriptionImg(String name, Double price, String description, String imgUrl) {
//        return new ProductDto(name, price, description, imgUrl);
//    }
}
