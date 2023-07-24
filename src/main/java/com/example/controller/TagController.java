package com.example.controller;

import com.example.dto.JwtDTO;
import com.example.dto.TagDTO;
import com.example.enums.ProfileRole;
import com.example.service.TagService;
import com.example.utility.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @PostMapping("")
    public ResponseEntity<TagDTO>create(@RequestBody TagDTO tagDTO,
                                        HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.MODERATOR);
        return ResponseEntity.ok(tagService.create(tagDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getById(@PathVariable Integer id,
                                          HttpServletRequest request){
        SecurityUtil.hasRole(request,ProfileRole.MODERATOR);
        return ResponseEntity.ok(tagService.getById(id));
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<TagDTO>> getAll(HttpServletRequest request){
        SecurityUtil.hasRole(request,ProfileRole.MODERATOR);
        return ResponseEntity.ok(tagService.getAll());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String>delete(@PathVariable Integer id,
                                        HttpServletRequest request){
        SecurityUtil.hasRole(request,ProfileRole.MODERATOR);
        return ResponseEntity.ok(tagService.delete(id));
    }


}
