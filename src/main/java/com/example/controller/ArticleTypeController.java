package com.example.controller;

import com.example.dto.ArticleTypeDTO;
import com.example.dto.JwtDTO;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.ArticleTypeService;
import com.example.utility.SecurityUtil;
import com.example.utility.SpringSecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/articleType")
public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<?> create(@Valid @RequestBody ArticleTypeDTO articleTypeDTO){
        Integer prtId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(articleTypeService.create(articleTypeDTO,prtId));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public ResponseEntity<?>update(@Valid @PathVariable Integer id,
                                   @RequestBody ArticleTypeDTO articleTypeDTO){
        Integer prtId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(articleTypeService.update(articleTypeDTO,id,prtId));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?>delete(@PathVariable Integer id){
        return ResponseEntity.ok(articleTypeService.delete(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/getAll")
    public ResponseEntity<?>getAll(@RequestParam("page") Integer page,
                                   @RequestParam("size") Integer size){
        return ResponseEntity.ok(articleTypeService.getAll(page-1, size));
    }
    @GetMapping("/public/language")
    public ResponseEntity<?>getByLang(@RequestParam(value = "lang",defaultValue = "Uz") Language lang){
        return ResponseEntity.ok(articleTypeService.getByLanguage(lang));
    }
}
