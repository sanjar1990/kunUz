package com.example.controller;
import com.example.dto.ArticleDTO;
import com.example.dto.FilterArticleDTO;
import com.example.dto.JwtDTO;
import com.example.enums.ArticleStatus;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.ArticleService;
import com.example.utility.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
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
    public ResponseEntity<ArticleDTO>create(@Valid @RequestBody ArticleDTO articleDTO,
                                            HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ROLE_MODERATOR);
        return ResponseEntity.ok(articleService.create(jwtDTO.getId(),articleDTO));
    }
    //update by moderator

    @PutMapping("/closed/{id}")
    public ResponseEntity<String>update(@Valid @RequestBody ArticleDTO articleDTO,
                                        @PathVariable String id,
                                        HttpServletRequest request){
        JwtDTO jwtDTO=SecurityUtil.hasRole(request,ProfileRole.ROLE_MODERATOR);
        return ResponseEntity.ok(articleService.update(articleDTO,jwtDTO.getId(),id));
    }
    // delete moderator
    @DeleteMapping("/closed/{id}")
    public ResponseEntity<String>delete(@PathVariable String id,
                                        HttpServletRequest request){
        JwtDTO jwtDTO=SecurityUtil.hasRole(request,ProfileRole.ROLE_MODERATOR);
        return ResponseEntity.ok(articleService.deleteArticle(id));
    }
    //update article status
    @PutMapping("/closed/updateStatus/{id}")
    public ResponseEntity<String>update(@PathVariable String id,
                                        @RequestParam("status") ArticleStatus status,
                                        HttpServletRequest request){
        JwtDTO jwtDTO=SecurityUtil.hasRole(request,ProfileRole.ROLE_PUBLISHER);
        return ResponseEntity.ok(articleService.updateStatus(id,status,jwtDTO.getId()));
    }
    //5)get five
    //6)get last three
    @GetMapping("/getLast")
    public ResponseEntity<List<ArticleDTO>>getLastFive(@RequestParam("typeId") Integer typeId,
                                                       @RequestParam("amount") Integer limit){
        return ResponseEntity.ok(articleService.getLastArticle(typeId, limit));
    }
    //7
    @GetMapping("/getLastEight")
    public ResponseEntity<List<ArticleDTO>>getLastEight(@RequestBody List<String> idList){
        return ResponseEntity.ok(articleService.getLastEight(idList));
    }
    // 8. Get Article By Id And Lang
    @GetMapping("/getByLang")
    public ResponseEntity<ArticleDTO>getByLang(@RequestParam("articleId") String articleId,
                                                          @RequestParam(value = "lang", defaultValue = "UZ") Language language){
        return ResponseEntity.ok(articleService.getArticleByIdAndLang(articleId, language));
    }
    //9. Get Last 4 Article By Types and except given article id.
    @GetMapping("/getLastFour/{articleId}")
    public ResponseEntity<List<ArticleDTO>>getLastFour(@PathVariable String articleId,
                                                       @RequestParam("typeId")Integer typeId
                                                       ){
        return ResponseEntity.ok(articleService.getLastFour(typeId,articleId));
    }
    //  10. Get 4 most read articles
    @GetMapping("/getMostViewed")
    public ResponseEntity<List<ArticleDTO>>mostRead(){
        return ResponseEntity.ok(articleService.fourMostRead());
    }
    // 11. Get Last 4 Article By TagName (Bitta article ni eng ohirida chiqib turadi.)
    @GetMapping("/getByTag")
    public ResponseEntity<List<ArticleDTO>>getByTag(@RequestParam("tag")String tag){
        return ResponseEntity.ok(articleService.getByTag(tag));
    }
    //12. Get Last 5 Article By Types  And By Region Key
    @GetMapping("/getByTypeAndRegion")
    public ResponseEntity<List<ArticleDTO>>getByTypeAndRegion(@RequestParam("typeId") Integer typeId,
                                                                 @RequestParam("regionId") Integer regionId){
        return ResponseEntity.ok(articleService.getByTypeAndRegion(typeId,regionId));
    }
    // 13. Get Article list by Region Key (Pagination)
    @GetMapping("/paginationByRegion")
    public ResponseEntity<PageImpl<ArticleDTO>>paginationByRegion(@RequestParam("regionId") Integer regionId,
                                                      @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                      @RequestParam(value = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok(articleService.paginationByRegion(regionId,page-1,size));
    }
    //14. Get Last 5 Article Category Key
    @GetMapping("/getLastFiveByCategory/{categoryId}")
    public ResponseEntity<List<ArticleDTO>>getLastFiveByCategory(@PathVariable Integer categoryId){
        return ResponseEntity.ok(articleService.getLastFiveByCategory(categoryId));
    }
    //15. Get Article By Category Key (Pagination)
    @GetMapping("/getByCategoryPagination/{categoryId}")
    public ResponseEntity<PageImpl<ArticleDTO>>getByCategoryPagination(@PathVariable Integer categoryId,
                                                                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                        @RequestParam(value = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok(articleService.getByCategoryPagination(categoryId,page-1,size));
    }
    //16. Increase Article View Count by Article Id
    @PutMapping("/increaseViewCount/{articleId}")
    public ResponseEntity<String>increaseViewCount(@PathVariable String articleId){
        return ResponseEntity.ok(articleService.increaseViewCount(articleId));
    }
    //17. Increase Share View Count by Article Id
    @PutMapping("/increaseShareCount/{articleId}")
    public ResponseEntity<String>increaseShareCount(@PathVariable String articleId){
        return ResponseEntity.ok(articleService.increaseShareCount(articleId));
    }
    //18ArticlePagination
    @PostMapping("/closed/filterPagination")
    public ResponseEntity<PageImpl<ArticleDTO>>filterPagination(@RequestBody FilterArticleDTO filterArticleDTO,
                                                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                @RequestParam(value = "size",defaultValue = "10") Integer size,
                                                                HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ROLE_PUBLISHER);
        return ResponseEntity.ok(articleService.filterPagination(filterArticleDTO, page - 1, size));
    }}