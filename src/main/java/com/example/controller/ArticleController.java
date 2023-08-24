package com.example.controller;
import com.example.config.CustomUserDetails;
import com.example.dto.ArticleDTO;
import com.example.dto.FilterArticleDTO;
import com.example.dto.JwtDTO;
import com.example.enums.ArticleStatus;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.ArticleService;
import com.example.utility.SecurityUtil;
import com.example.utility.SpringSecurityUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Tag(name = "Article", description = "Article api list.")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    // create article by moderator
    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("")
    @Operation(summary = "Create article", description = "This api used for article creation...")
    public ResponseEntity<ArticleDTO>create(@Valid @RequestBody ArticleDTO articleDTO){
        return ResponseEntity.ok(articleService.create(SpringSecurityUtil.getProfileId(),articleDTO));
    }
    //update by moderator
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR','ROLE_PUBLISHER')")
    @PutMapping("/{id}")
    @Operation(summary = "update article by moderator ", description = "This api used for article update...")
    public ResponseEntity<String>update(@Valid @RequestBody ArticleDTO articleDTO,
                                        @PathVariable String id){
        return ResponseEntity.ok(articleService.update(articleDTO,SpringSecurityUtil.getProfileId(),id));
    }
    // delete moderator
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR','ROLE_PUBLISHER')")
    @DeleteMapping("/{id}")
    @Operation(summary = "delete article by moderator ", description = "This api used for deleting article ...")
    public ResponseEntity<String>delete(@PathVariable String id){
        return ResponseEntity.ok(articleService.deleteArticle(id));
    }
    //update article status
    @PreAuthorize("hasRole('ROLE_PUBLISHER')")
    @PutMapping("/updateStatus/{id}")
    @Operation(summary = "update article status by publisher ", description = "This api used for updating status article ...")
    public ResponseEntity<String>update(@PathVariable String id,
                                        @RequestParam("status") ArticleStatus status){
        return ResponseEntity.ok(articleService.updateStatus(id,status,SpringSecurityUtil.getProfileId()));
    }
    //5)get five
    //6)get last three
    @GetMapping("/public/getLast")
    @Operation(summary = "get last article ", description = "This api used for getting last article entering amount and type ...")
    public ResponseEntity<List<ArticleDTO>>getLastFive(@RequestParam("typeId") Integer typeId,
                                                       @RequestParam("amount") Integer limit){
        return ResponseEntity.ok(articleService.getLastArticle(typeId, limit));
    }
    //7
    @GetMapping("/public/getLastEight")
    @Operation(summary = " getLastEight ", description = "This api used for getting last eight  ...")
    public ResponseEntity<List<ArticleDTO>>getLastEight(@RequestBody List<String> idList){
        return ResponseEntity.ok(articleService.getLastEight(idList));
    }
    // 8. Get Article By Id And Lang
    @GetMapping("/public/getByLang")
    public ResponseEntity<ArticleDTO>getByLang(@RequestParam("articleId") String articleId,
                                                          @RequestParam(value = "lang", defaultValue = "UZ") Language language){
        return ResponseEntity.ok(articleService.getArticleByIdAndLang(articleId, language));
    }
    //9. Get Last 4 Article By Types and except given article id.
    @GetMapping("/public/getLastFour/{articleId}")
    public ResponseEntity<List<ArticleDTO>>getLastFour(@PathVariable String articleId,
                                                       @RequestParam("typeId")Integer typeId){
        return ResponseEntity.ok(articleService.getLastFour(typeId,articleId));
    }
    //  10. Get 4 most read articles
    @GetMapping("/public/getMostViewed")
    public ResponseEntity<List<ArticleDTO>>mostRead(){
        return ResponseEntity.ok(articleService.fourMostRead());
    }
    // 11. Get Last 4 Article By TagName (Bitta article ni eng ohirida chiqib turadi.)
    @GetMapping("/public/getByTag")
    public ResponseEntity<List<ArticleDTO>>getByTag(@RequestParam("tag")String tag){
        return ResponseEntity.ok(articleService.getByTag(tag));
    }
    //12. Get Last 5 Article By Types  And By Region Key
    @GetMapping("/public/getByTypeAndRegion")
    public ResponseEntity<List<ArticleDTO>>getByTypeAndRegion(@RequestParam("typeId") Integer typeId,
                                                                 @RequestParam("regionId") Integer regionId){
        return ResponseEntity.ok(articleService.getByTypeAndRegion(typeId,regionId));
    }
    // 13. Get Article list by Region Key (Pagination)
    @GetMapping("/public/paginationByRegion")
    public ResponseEntity<PageImpl<ArticleDTO>>paginationByRegion(@RequestParam("regionId") Integer regionId,
                                                      @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                      @RequestParam(value = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok(articleService.paginationByRegion(regionId,page-1,size));
    }
    //14. Get Last 5 Article Category Key
    @GetMapping("/public/getLastFiveByCategory/{categoryId}")
    public ResponseEntity<List<ArticleDTO>>getLastFiveByCategory(@PathVariable Integer categoryId){
        return ResponseEntity.ok(articleService.getLastFiveByCategory(categoryId));
    }
    //15. Get Article By Category Key (Pagination)
    @GetMapping("/public/getByCategoryPagination/{categoryId}")
    public ResponseEntity<PageImpl<ArticleDTO>>getByCategoryPagination(@PathVariable Integer categoryId,
                                                                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                        @RequestParam(value = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok(articleService.getByCategoryPagination(categoryId,page-1,size));
    }
    //16. Increase Article View Count by Article Id
    @PutMapping("/public/increaseViewCount/{articleId}")
    public ResponseEntity<String>increaseViewCount(@PathVariable String articleId){
        return ResponseEntity.ok(articleService.increaseViewCount(articleId));
    }
    //17. Increase Share View Count by Article Id
    @PutMapping("/public/increaseShareCount/{articleId}")
    public ResponseEntity<String>increaseShareCount(@PathVariable String articleId){
        return ResponseEntity.ok(articleService.increaseShareCount(articleId));
    }
    //18ArticlePagination
    @PreAuthorize("hasRole('ROLE_PUBLISHER')")
    @PostMapping("/closed/filterPagination")
    public ResponseEntity<PageImpl<ArticleDTO>>filterPagination(@RequestBody FilterArticleDTO filterArticleDTO,
                                                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                @RequestParam(value = "size",defaultValue = "10") Integer size) {
        return ResponseEntity.ok(articleService.filterPagination(filterArticleDTO, page - 1, size));
    }}