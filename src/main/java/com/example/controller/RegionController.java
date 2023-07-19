package com.example.controller;
import com.example.dto.JwtDTO;
import com.example.dto.RegionDTO;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.RegionService;
import com.example.utility.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping({"","/"})
    public ResponseEntity<?> createRegion(@RequestBody RegionDTO regionDTO,
                                          @RequestHeader ("Authorization") String authToken){
        JwtDTO jwtDTO= SecurityUtil.checkRoleForAdmin(authToken, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.createRegion(regionDTO));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?>updateRegion(@PathVariable Integer id,
                                         @RequestBody RegionDTO regionDTO,
                                         @RequestHeader ("Authorization") String authToken){
        JwtDTO jwtDTO= SecurityUtil.checkRoleForAdmin(authToken, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.updateRegion(regionDTO,id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleteById(@PathVariable Integer id,
                                       @RequestHeader ("Authorization") String authToken){
        JwtDTO jwtDTO= SecurityUtil.checkRoleForAdmin(authToken, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.deleteRegion(id));
    }
    @GetMapping("/getAll")
    public ResponseEntity<?>getAll( @RequestHeader ("Authorization") String authToken){
        JwtDTO jwtDTO= SecurityUtil.checkRoleForAdmin(authToken, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.getAllRegion());
    }
    @GetMapping("/language")
    public ResponseEntity<?>getByLanguage(@RequestParam(value = "lang",defaultValue = "Uz") Language language,
                                          @RequestHeader ("Authorization") String authToken){
        JwtDTO jwtDTO= SecurityUtil.checkRoleForAdmin(authToken, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.getByLanguage(language));
    }
}
