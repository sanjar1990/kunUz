package com.example.controller;
import com.example.dto.JwtDTO;
import com.example.dto.RegionDTO;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.RegionService;
import com.example.utility.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.example.utility.SpringSecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/region")
public class RegionController {
    @Autowired
    private RegionService regionService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping({""})
    public ResponseEntity<?> createRegion(@Valid @RequestBody RegionDTO regionDTO){
            Integer prtId=SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(regionService.createRegion(regionDTO,prtId));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?>updateRegion(@Valid @PathVariable Integer id,
                                         @RequestBody RegionDTO regionDTO){
        Integer prtId=SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(regionService.updateRegion(regionDTO,id, prtId));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleteById(@PathVariable Integer id,
                                       Principal principal){
        String name=principal.getName();
        return ResponseEntity.ok(regionService.deleteRegion(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<?>getAll(Authentication authentication){
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        String userName= SpringSecurityUtil.getCurrentUserName();
        return ResponseEntity.ok(regionService.getAllRegion());
    }
    @GetMapping("/public/language")
    public ResponseEntity<?>getByLanguage(@RequestParam(value = "lang",defaultValue = "Uz") Language language){
        return ResponseEntity.ok(regionService.getByLanguage(language));
    }
}
