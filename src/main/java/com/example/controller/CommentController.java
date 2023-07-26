package com.example.controller;

import com.example.dto.CommentDTO;
import com.example.dto.FilterCommentDTO;
import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.service.CommentService;
import com.example.utility.SecurityUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.PeriodUnit;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    // 1. CREATE (ANY)
    //        (content,article_id,reply_id)
    @PostMapping("")
    public ResponseEntity<CommentDTO>create(@RequestBody CommentDTO commentDTO,
                                            HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request,null);
        System.out.println(jwtDTO.getId());
        return ResponseEntity.ok(commentService.create(jwtDTO.getId(),commentDTO));
    }
//    2. UPDATE (ANY and owner)
//         (content,article_id)
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO>update(@PathVariable String commentId,
                                        @RequestBody CommentDTO commentDTO,
                                        HttpServletRequest request){
        JwtDTO jwtDTO=SecurityUtil.hasRole(request,null);
        return ResponseEntity.ok(commentService.update(commentId,jwtDTO.getId(),commentDTO));
    }
    //3. DELETE (ADMIN,ANY(only owner))
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String>delete(@PathVariable String commentId,
                                        HttpServletRequest request){
        JwtDTO jwtDTO=SecurityUtil.hasRole(request, null);
        return ResponseEntity.ok(commentService.delete(commentId,jwtDTO.getId()));
    }
//     4. Get Article Comment List By Article Id
//    id,created_date,update_date,profile(id,name,surname)
    @GetMapping("/getByArticleId/{articleId}")
    public ResponseEntity<List<CommentDTO>>getByArticleId(@PathVariable String articleId,
                                                          HttpServletRequest request){
        JwtDTO jwtDTO=SecurityUtil.hasRole(request,  null);
        return ResponseEntity.ok(commentService.getByArticleId(articleId,jwtDTO.getId()));
    }
//      5. Comment List (pagination) (ADMIN)
//            (id,created_date,update_date,profile(id,name,surname),content,article(id,title),reply_id,)
    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<CommentDTO>>pagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                          HttpServletRequest request){
        JwtDTO jwtDTO=SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(commentService.pagination(page-1,size));
    }
//    6. Comment Filter(id,created_date_from,created_date_to,profile_id,article_id) with Pagination (ADMIN)
//    id,created_date,update_date,profile_id,content,article_id,reply_id,visible
    @PostMapping("/filterPagination")
    public ResponseEntity<?>filterPagination(@RequestBody FilterCommentDTO filterCommentDTO,
                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                                             HttpServletRequest request){
        JwtDTO jwtDTO=SecurityUtil.hasRole(request,ProfileRole.ADMIN);
        return ResponseEntity.ok(commentService.filterPagination(filterCommentDTO,page-1,size));
    }
//     7. Get Replied Comment List by Comment Id
//    id,created_date,update_date,profile(id,name,surname)
    @GetMapping("/getRepliedComment/{commentId}")
    public ResponseEntity<List<CommentDTO>>getRepliedComment(@PathVariable String commentId,
                                                             HttpServletRequest request){
        JwtDTO jwtDTO=SecurityUtil.hasRole(request, null);
        return ResponseEntity.ok(commentService.getRepliedComment(commentId));
    }
}
