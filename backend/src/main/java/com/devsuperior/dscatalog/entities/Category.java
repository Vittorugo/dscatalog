package com.devsuperior.dscatalog.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tb_categories")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

//
//    public static class Builder {
//        private Long id;
//        private String name;
//
//        public Builder(Long id, String name) {
//            this.id = id;
//            this.name = name;
//        }
//
//        public Builder novoParametro(Object obj) {
//            Object object = obj;
//            return this;
//        }
//
//         public  Category build() {
//            return new Category(this);
//         }
//    }
//
//    public Category(Builder builder) {
//        this.id = builder.id;
//        this.name = builder.name;
//    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
