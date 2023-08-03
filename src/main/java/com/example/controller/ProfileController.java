package com.example.controller;
import com.example.dto.FilterProfileDTO;
import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.ProfileUpdateDetailDTO;
import com.example.enums.ProfileRole;
import com.example.service.ProfileService;
import com.example.utility.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @PostMapping("")
    public ResponseEntity<?> createProfile(@Valid @RequestBody ProfileDTO profileDTO,
                                           HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.createProfile(profileDTO,jwtDTO.getId()));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> staffUpdateByAdmin(@Valid @RequestBody ProfileDTO profileDTO,
                                                @PathVariable Integer id,
                                                HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.staffUpdateByAdmin(profileDTO,id));
    }
    @PutMapping("/updateDetail")
    public ResponseEntity<?>updateProfile(@RequestBody ProfileUpdateDetailDTO dto,
                                               HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, null);
        return ResponseEntity.ok(profileService.updateProfile(dto,jwtDTO.getId()));
    }

    //6. Update Photo (ANY)
    @PutMapping("/updatePhoto")
    public ResponseEntity<String >updatePhoto(@RequestParam("photoId") String photoId,
                              HttpServletRequest request){
        JwtDTO jwtDTO=SecurityUtil.hasRole(request,null);
        return ResponseEntity.ok(profileService.updatePhoto(jwtDTO.getId(),photoId));
    }
    @GetMapping("/profileListPagination")
    public ResponseEntity<?>profileListPagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                  @RequestParam(value = "size",defaultValue = "10") Integer size,
                                                  HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.profileListPagination(page-1,size));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleteProfileById(@PathVariable Integer id,
                                              HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.deleteProfile(id));
    }
    @PostMapping("/filterPagination")
    public ResponseEntity<?>filterPagination(@RequestBody FilterProfileDTO filterProfileDTO,
                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                                             HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.filterPagination(filterProfileDTO,page-1,size));
    }
}
