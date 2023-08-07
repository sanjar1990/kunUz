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

    @PostMapping({"/admin/"})
    public ResponseEntity<?> createRegion(@Valid @RequestBody RegionDTO regionDTO){
//        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ROLE_ADMIN);
        return ResponseEntity.ok(regionService.createRegion(regionDTO,1));
    }
    @PutMapping("/admin/{id}")
    public ResponseEntity<?>updateRegion(@Valid @PathVariable Integer id,
                                         @RequestBody RegionDTO regionDTO,
                                         HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ROLE_ADMIN);
        return ResponseEntity.ok(regionService.updateRegion(regionDTO,id,jwtDTO.getId()));
    }
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?>deleteById(@PathVariable Integer id,
                                       Principal principal){
        String name=principal.getName();
        return ResponseEntity.ok(regionService.deleteRegion(id));
    }
    @GetMapping("/admin/getAll")
    public ResponseEntity<?>getAll(Authentication authentication){
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        String userName= SpringSecurityUtil.getCurrentUserName();
        return ResponseEntity.ok(regionService.getAllRegion());
    }
    @GetMapping("/language")
    public ResponseEntity<?>getByLanguage(@RequestParam(value = "lang",defaultValue = "Uz") Language language){
        return ResponseEntity.ok(regionService.getByLanguage(language));
    }
}
