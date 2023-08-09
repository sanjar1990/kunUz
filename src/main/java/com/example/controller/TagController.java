package com.example.controller;

import com.example.dto.JwtDTO;
import com.example.dto.TagDTO;
import com.example.enums.ProfileRole;
import com.example.service.TagService;
import com.example.utility.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {
    @Autowired
    private TagService tagService;
    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("")
    public ResponseEntity<TagDTO>create(@Valid @RequestBody TagDTO tagDTO){

        return ResponseEntity.ok(tagService.create(tagDTO));
    }
    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getById(@PathVariable Integer id){
        return ResponseEntity.ok(tagService.getById(id));
    }
    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/getAll")
    public ResponseEntity<List<TagDTO>> getAll(){
        return ResponseEntity.ok(tagService.getAll());
    }
    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String>delete(@PathVariable Integer id){
        return ResponseEntity.ok(tagService.delete(id));
    }
}
