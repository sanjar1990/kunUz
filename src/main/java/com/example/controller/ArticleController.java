package com.example.controller;
import com.example.dto.ArticleDTO;
import com.example.dto.JwtDTO;
import com.example.entity.ArticleEntity;
import com.example.enums.ArticleStatus;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.mapper.ArticleFullInfoMapper;
import com.example.mapper.ArticleMapperInterface;
import com.example.service.ArticleService;
import com.example.utility.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
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
        return ResponseEntity.ok(articleService.update(articleDTO,jwtDTO.getId(),id));
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
                                        @RequestParam("status") ArticleStatus status,
                                        HttpServletRequest request){
        JwtDTO jwtDTO=SecurityUtil.hasRole(request,ProfileRole.PUBLISHER);
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

    @GetMapping("/paginationByRegion")
    public ResponseEntity<PageImpl<ArticleDTO>>paginationByRegion(@RequestParam("regionId") Integer regionId,
                                                      @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                      @RequestParam(value = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok(articleService.paginationByRegion(regionId,page-1,size));
    }
}
