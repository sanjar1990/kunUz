package com.example.controller;

import com.example.dto.FilterProfileDTO;
import com.example.dto.ProfileDTO;
import com.example.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @PostMapping("/createStaff")
    public ResponseEntity<?> createProfile(@RequestBody ProfileDTO profileDTO){
        return ResponseEntity.ok(profileService.createProfile(profileDTO));
    }
    @PutMapping("/staffUpdateByAdmin/{id}")
    public ResponseEntity<?> staffUpdateByAdmin(@RequestBody ProfileDTO profileDTO,
                                                @PathVariable Integer id){
        return ResponseEntity.ok(profileService.staffUpdateByAdmin(profileDTO,id));
    }
    @PutMapping("/updateStaffByStaff/{id}")
    public ResponseEntity<?>updateStaffByStaff(@RequestBody ProfileDTO profileDTO,
                                                @PathVariable Integer id){
        return ResponseEntity.ok(profileService.updateStaffByStaff(profileDTO,id));
    }
    @GetMapping("/profileListPagination")
    public ResponseEntity<?>profileListPagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                         @RequestParam(value = "size",defaultValue = "10") Integer size){
        return ResponseEntity.ok(profileService.profileListPagination(page-1,size));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>deleteProfileById(@PathVariable Integer id){
        return ResponseEntity.ok(profileService.deleteProfile(id));
    }
    @PostMapping("/filterPagination")
    public ResponseEntity<?>filterPagination(@RequestBody FilterProfileDTO filterProfileDTO,
                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok(profileService.filterPagination(filterProfileDTO,page-1,size));
    }


}
