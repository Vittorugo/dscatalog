package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private Double price;
    private String description;
    private String imgUrl;

    private Instant date;

    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.imgUrl = product.getImgUrl();
        this.date = product.getDate();
    }


    public ProductDto(Product product, Set<Category> categories) {
        this(product);
        categories.forEach( category -> this.categories.add(new CategoryDTO(category)));
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
