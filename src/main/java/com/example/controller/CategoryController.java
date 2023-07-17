package com.example.controller;

import com.example.dto.CategoryDTO;
import com.example.enums.Language;
import com.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping("")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO){
        return ResponseEntity.ok(categoryService.createCategory(categoryDTO));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?>updateCategory(@PathVariable Integer id,
                                           @RequestBody CategoryDTO categoryDTO){
        return ResponseEntity.ok(categoryService.updateCategory(categoryDTO,id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleteCategory(@PathVariable Integer id){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
    @GetMapping("/getAll")
    public ResponseEntity<?>getAll(){
        return ResponseEntity.ok(categoryService.getAll());
    }
    @GetMapping("/language")
    public ResponseEntity<?>getByLang(@RequestParam(value = "lang", defaultValue = "Uz") Language language){
        return ResponseEntity.ok(categoryService.getByLang(language));
    }
}
