package com.example.controller;
import com.example.dto.FilterProfileDTO;
import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.ProfileUpdateDetailDTO;
import com.example.enums.ProfileRole;
import com.example.service.ProfileService;
import com.example.utility.SecurityUtil;
import com.example.utility.SpringSecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createProfile(@Valid @RequestBody ProfileDTO profileDTO){
        Integer prtId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(profileService.createProfile(profileDTO,prtId));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> staffUpdateByAdmin(@Valid @RequestBody ProfileDTO profileDTO,
                                                @PathVariable Integer id){
        return ResponseEntity.ok(profileService.staffUpdateByAdmin(profileDTO,id));
    }
    @PutMapping("/updateDetail")
    public ResponseEntity<?>updateProfile(@RequestBody ProfileUpdateDetailDTO dto){
        Integer prtId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(profileService.updateProfile(dto,prtId));
    }

    //6. Update Photo (ANY)
    @PutMapping("/updatePhoto")
    public ResponseEntity<String >updatePhoto(@RequestParam("photoId") String photoId){
        Integer prtId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(profileService.updatePhoto(prtId,photoId));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/profileListPagination")
    public ResponseEntity<?>profileListPagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                  @RequestParam(value = "size",defaultValue = "10") Integer size){
        return ResponseEntity.ok(profileService.profileListPagination(page-1,size));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleteProfileById(@PathVariable Integer id){
        return ResponseEntity.ok(profileService.deleteProfile(id));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/filterPagination")
    public ResponseEntity<?>filterPagination(@RequestBody FilterProfileDTO filterProfileDTO,
                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok(profileService.filterPagination(filterProfileDTO,page-1,size));
    }
}
