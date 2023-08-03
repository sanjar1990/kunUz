package com.example.controller;
import com.example.dto.CategoryDTO;
import com.example.dto.JwtDTO;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.CategoryService;
import com.example.utility.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping("/admin")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                            HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.createCategory(categoryDTO,jwtDTO.getId()));
    }
    @PutMapping("/admin/{id}")
    public ResponseEntity<?>updateCategory(@PathVariable Integer id,
                                           @Valid @RequestBody CategoryDTO categoryDTO,
                                          HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.updateCategory(categoryDTO,id,jwtDTO.getId()));
    }
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?>deleteCategory(@PathVariable Integer id,
                                           HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
    @GetMapping("/admin/getAll")
    public ResponseEntity<?>getAll(HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.getAll());
    }
    @GetMapping("/language")
    public ResponseEntity<?>getByLang(@RequestParam(value = "lang", defaultValue = "Uz") Language language){
        return ResponseEntity.ok(categoryService.getByLang(language));
    }
}
