package com.example.controller;

import com.example.dto.ArticleDTO;
import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.mapper.ArticleTypeListMapper;
import com.example.service.ArticleService;
import com.example.utility.JWTUtil;
import com.example.utility.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    // create article by moderator
    @PostMapping("/closed")
    public ResponseEntity<ArticleDTO>create(@RequestBody ArticleDTO articleDTO,
                                            HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.create(jwtDTO.getId(),articleDTO));
    }
    //update by moderator

    @PutMapping("/closed/{id}")
    public ResponseEntity<String>update(@RequestBody ArticleDTO articleDTO,
                                        @PathVariable String id,
                                        HttpServletRequest request){
        JwtDTO jwtDTO=SecurityUtil.hasRole(request,ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService. update(articleDTO,jwtDTO.getId(),id));
    }
    // delete moderator
    @DeleteMapping("/closed/{id}")
    public ResponseEntity<String>delete(@PathVariable String id,
                                        HttpServletRequest request){
        JwtDTO jwtDTO=SecurityUtil.hasRole(request,ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.deleteArticle(id));
    }
    //update article status
    @PutMapping("/closed/updateStatus/{id}")
    public ResponseEntity<String>update(@PathVariable String id,
                                        @RequestBody ArticleDTO articleDTO,
                                        HttpServletRequest request){
        JwtDTO jwtDTO=SecurityUtil.hasRole(request,ProfileRole.PUBLISHER);
        return ResponseEntity.ok(articleService.updateStatus(id,articleDTO));
    }
    //get five
    @GetMapping("/getLastFive")
    public ResponseEntity<?>getLastFive(@RequestBody ArticleTypeListMapper articleTypeListMapper){
        return ResponseEntity.ok(articleService.getLastFiveArticle(articleTypeListMapper));
    }
    //get last three
    @GetMapping("/getLastThree")
    public ResponseEntity<?>getLastThree(@RequestBody ArticleTypeListMapper articleTypeListMapper){
        return ResponseEntity.ok(articleService.getLastThreeByType(articleTypeListMapper));
    }
    @GetMapping("/getLastEight")
    public ResponseEntity<?>getLastEight(@RequestBody List<String> idList){
        return ResponseEntity.ok(articleService.getLastEight(idList));
    }

}
