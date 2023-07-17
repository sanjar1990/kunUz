package com.example.controller;

import com.example.dto.ArticleTypeDTO;
import com.example.enums.Language;
import com.example.service.ArticleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/articleType")
public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody ArticleTypeDTO articleTypeDTO){
        return ResponseEntity.ok(articleTypeService.create(articleTypeDTO));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?>update(@PathVariable Integer id,
                                   @RequestBody ArticleTypeDTO articleTypeDTO){
        return ResponseEntity.ok(articleTypeService.update(articleTypeDTO,id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?>delete(@PathVariable Integer id){
        return ResponseEntity.ok(articleTypeService.delete(id));
    }
    @GetMapping("/getAll")
    public ResponseEntity<?>getAll(@RequestParam("page") Integer page,
                                   @RequestParam("size") Integer size){
        return ResponseEntity.ok(articleTypeService.getAll(page-1, size));
    }
    @GetMapping("/language")
    public ResponseEntity<?>getByLang(@RequestParam(value = "lang",defaultValue = "Uz") Language lang){
        return ResponseEntity.ok(articleTypeService.getByLanguage(lang));
    }
}
