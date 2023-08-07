package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.SavedArticleDTO;
import com.example.entity.SavedArticleEntity;
import com.example.service.SavedArticleService;
import com.example.utility.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/savedArticle")
public class SavedArticleController {
    @Autowired
    private SavedArticleService savedArticleService;
    @PostMapping("/{articleId}")
    public ResponseEntity<ApiResponseDTO>save(@PathVariable String articleId){
        Integer prtId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(savedArticleService.save(articleId,prtId));
    }
    @DeleteMapping("/{articleId}")
    public ResponseEntity<ApiResponseDTO>delete(@PathVariable String articleId){
        Integer prtId=SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(savedArticleService.delete(articleId,prtId));
    }
    @GetMapping("")
    public ResponseEntity<List<SavedArticleDTO>>getAll(){
        Integer prtId=SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(savedArticleService.getsavedArticleList(prtId));
    }
}
