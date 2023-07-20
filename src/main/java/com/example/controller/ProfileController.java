package com.example.controller;

import com.example.dto.FilterProfileDTO;
import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.enums.ProfileRole;
import com.example.service.ProfileService;
import com.example.utility.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @PostMapping("")
    public ResponseEntity<?> createProfile(@RequestBody ProfileDTO profileDTO,
                                           HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.createProfile(profileDTO,jwtDTO.getId()));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> staffUpdateByAdmin(@RequestBody ProfileDTO profileDTO,
                                                @PathVariable Integer id,
                                                @RequestHeader ("Authorization") String authToken){
        JwtDTO jwtDTO= SecurityUtil.checkRoleForAdmin(authToken, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.staffUpdateByAdmin(profileDTO,id));
    }
    @PutMapping("/updateDetail")
    public ResponseEntity<?>updateStaffByStaff(@RequestBody ProfileDTO profileDTO,
                                               HttpServletRequest request){
        JwtDTO jwtDTO= SecurityUtil.hasRole(request,null);
        return ResponseEntity.ok(profileService.updateStaffByStaff(profileDTO,null));
    }
    @GetMapping("/profileListPagination")
    public ResponseEntity<?>profileListPagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                  @RequestParam(value = "size",defaultValue = "10") Integer size,
                                                  @RequestHeader ("Authorization") String authToken){
        JwtDTO jwtDTO= SecurityUtil.checkRoleForAdmin(authToken, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.profileListPagination(page-1,size));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleteProfileById(@PathVariable Integer id,
                                              @RequestHeader ("Authorization") String authToken){
        JwtDTO jwtDTO= SecurityUtil.checkRoleForAdmin(authToken, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.deleteProfile(id));
    }
    @PostMapping("/filterPagination")
    public ResponseEntity<?>filterPagination(@RequestBody FilterProfileDTO filterProfileDTO,
                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                                             HttpServletRequest request){

        return ResponseEntity.ok(profileService.filterPagination(filterProfileDTO,page-1,size));
    }
}
