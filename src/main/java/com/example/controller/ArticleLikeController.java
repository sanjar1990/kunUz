package com.example.controller;

import com.example.dto.ArticleLikeDTO;
import com.example.service.ArticleLikeService;
import com.example.utility.SpringSecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/articleLike")
public class ArticleLikeController {
    @Autowired
    private ArticleLikeService articleLikeService;

    @PostMapping("/like/{articleId}")
    public ResponseEntity<ArticleLikeDTO>like(@PathVariable String articleId){
        Integer prtId=SpringSecurityUtil.getProfileId();

        return ResponseEntity.ok(articleLikeService.like(articleId,prtId));
    }
    @PostMapping("/dislike/{articleId}")
    public ResponseEntity<ArticleLikeDTO>dislike(@PathVariable String articleId){
        Integer prtId=SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(articleLikeService.dislike(articleId,prtId));
    }
    @DeleteMapping("/remove/{articleLikeId}")
    public ResponseEntity<String>remove(@PathVariable String articleLikeId){
        return ResponseEntity.ok(articleLikeService.remove(articleLikeId));
    }
}
