package com.example.controller;

import com.example.dto.RegionDTO;
import com.example.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping("/create")
    public ResponseEntity<?> createRegion(@RequestBody RegionDTO regionDTO){
        return ResponseEntity.ok(regionService.createRegion(regionDTO));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?>updateRegion(@PathVariable Integer id,
                                         @RequestBody RegionDTO regionDTO){
        return ResponseEntity.ok(regionService.updateRegion(regionDTO,id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>deleteById(@PathVariable Integer id){
        return ResponseEntity.ok(regionService.deleteRegion(id));
    }
    @GetMapping("/getAll")
    public ResponseEntity<?>getAll(){
        return ResponseEntity.ok(regionService.getAllRegion());
    }
    @GetMapping("/language")
    public ResponseEntity<?>getByLanguage(@RequestParam("lang")String language){
        return ResponseEntity.ok(regionService.getByLanguage(language));
    }
}
