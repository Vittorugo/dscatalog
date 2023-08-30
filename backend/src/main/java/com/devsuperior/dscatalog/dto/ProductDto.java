package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductDto {

    private Long id;

    @Size(max = 60, message = "Nome não pode ultrapassar 60 caracteres")
    @NotBlank(message = "O nome não pode ser em branco")
    private String name;

    @Positive(message = "Preço deve ser um valor positivo")
    private Double price;
    private String description;
    private String imgUrl;

    @PastOrPresent(message = "A data do produto não pode ser futura")
    private Instant date;

    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.imgUrl = product.getImgUrl();
        this.date = product.getDate();

        product.getCategories().forEach( category -> this.getCategories().add(new CategoryDTO(category)));
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
