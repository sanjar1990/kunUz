package com.example.controller;

import com.example.dto.CommentLikeDTO;
import com.example.dto.JwtDTO;
import com.example.service.CommentLikeService;
import com.example.utility.SecurityUtil;
import com.example.utility.SpringSecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/commentLike")
public class CommentLikeController {
    @Autowired
    private CommentLikeService commentLikeService;
    //1. Like (ANY)
    @PostMapping("/like/{commentId}")
    public ResponseEntity<CommentLikeDTO>like(@PathVariable String commentId){
        Integer prtId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(commentLikeService.like(commentId,prtId));
    }
    //2. Dislike (ANY
    @PostMapping("/dislike/{commentId}")
    public ResponseEntity<CommentLikeDTO>dislike(@PathVariable String commentId){
        Integer prtId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(commentLikeService.dislike(commentId,prtId));
    }
    // 3. Remove (ANY
    @DeleteMapping("/{id}")
    public ResponseEntity<String>remove(@PathVariable String id){
        Integer prtId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(commentLikeService.remove(id,prtId));
    }
}
