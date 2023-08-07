package com.example.controller;
import com.example.dto.CategoryDTO;
import com.example.dto.JwtDTO;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.CategoryService;
import com.example.utility.SecurityUtil;
import com.example.utility.SpringSecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        Integer prtId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(categoryService.createCategory(categoryDTO,prtId));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public ResponseEntity<?>updateCategory(@PathVariable Integer id,
                                           @Valid @RequestBody CategoryDTO categoryDTO){
        Integer prtId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(categoryService.updateCategory(categoryDTO,id,prtId));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?>deleteCategory(@PathVariable Integer id){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<?>getAll(){
        return ResponseEntity.ok(categoryService.getAll());
    }
    @GetMapping("/public/language")
    public ResponseEntity<?>getByLang(@RequestParam(value = "lang", defaultValue = "Uz") Language language){
        return ResponseEntity.ok(categoryService.getByLang(language));
    }
}
