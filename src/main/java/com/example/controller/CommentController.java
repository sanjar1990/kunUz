package com.example.controller;

import com.example.dto.CommentDTO;
import com.example.dto.FilterCommentDTO;
import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.service.CommentService;
import com.example.utility.SecurityUtil;
import com.example.utility.SpringSecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<CommentDTO>create(@Valid @RequestBody CommentDTO commentDTO){
        Integer prtId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(commentService.create(prtId,commentDTO));
    }
//    2. UPDATE (ANY and owner)
//         (content,article_id)
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO>update(@PathVariable String commentId,
                                        @Valid @RequestBody CommentDTO commentDTO){
        Integer prtId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(commentService.update(commentId,prtId,commentDTO));
    }
    //3. DELETE (ADMIN,ANY(only owner))
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String>delete(@PathVariable String commentId){
        Integer prtId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(commentService.delete(commentId,prtId));
    }
//     4. Get Article Comment List By Article Id
//    id,created_date,update_date,profile(id,name,surname)
    @GetMapping("/public/getByArticleId/{articleId}")
    public ResponseEntity<List<CommentDTO>>getByArticleId(@PathVariable String articleId){

        return ResponseEntity.ok(commentService.getByArticleId(articleId));
    }
//      5. Comment List (pagination) (ADMIN)
//            (id,created_date,update_date,profile(id,name,surname),content,article(id,title),reply_id,)
   @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<CommentDTO>>pagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok(commentService.pagination(page-1,size));
    }
//    6. Comment Filter(id,created_date_from,created_date_to,profile_id,article_id) with Pagination (ADMIN)
//    id,created_date,update_date,profile_id,content,article_id,reply_id,visible
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/filterPagination")
    public ResponseEntity<?>filterPagination(@RequestBody FilterCommentDTO filterCommentDTO,
                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok(commentService.filterPagination(filterCommentDTO,page-1,size));
    }
//     7. Get Replied Comment List by Comment Id
//    id,created_date,update_date,profile(id,name,surname)
    @GetMapping("/public/getRepliedComment/{commentId}")
    public ResponseEntity<List<CommentDTO>>getRepliedComment(@PathVariable String commentId){
        return ResponseEntity.ok(commentService.getRepliedComment(commentId));
    }
}
