package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.imgUrl = product.getImgUrl();
    }

    private ProductDto(Product product, Set<Category> categories) {
        this(product);
        categories.forEach(category -> this.categories.add(new CategoryDTO(category)));
    }

    public static ProductDto withCategories(Product product, Set<Category> categories) {
        return new ProductDto(product, categories);
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


    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
