package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.entities.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @GetMapping
    public ResponseEntity<List<Category>> findAll() {
        List<Category> list = new ArrayList<>();
        list.add( new Category(1L, "Books"));
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody Category newCategory) {
        return ResponseEntity.ok().body("Saved Successfully");
    }
}
