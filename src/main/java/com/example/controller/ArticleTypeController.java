package com.example.controller;

import com.example.dto.ArticleTypeDTO;
import com.example.dto.JwtDTO;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.ArticleTypeService;
import com.example.utility.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/articleType")
public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;
    @PostMapping("/admin")
    public ResponseEntity<?> create(@RequestBody ArticleTypeDTO articleTypeDTO,
                                    HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.create(articleTypeDTO,jwtDTO.getId()));
    }
    @PutMapping("/admin/{id}")
    public ResponseEntity<?>update(@PathVariable Integer id,
                                   @RequestBody ArticleTypeDTO articleTypeDTO,
                                   HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.update(articleTypeDTO,id,jwtDTO.getId()));
    }
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?>delete(@PathVariable Integer id,
                                   HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.delete(id));
    }
    @GetMapping("/admin/getAll")
    public ResponseEntity<?>getAll(@RequestParam("page") Integer page,
                                   @RequestParam("size") Integer size,
                                    HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.getAll(page-1, size));
    }
    @GetMapping("/language")
    public ResponseEntity<?>getByLang(@RequestParam(value = "lang",defaultValue = "Uz") Language lang){
        return ResponseEntity.ok(articleTypeService.getByLanguage(lang));
    }
}
