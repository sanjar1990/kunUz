package com.example.controller;

import com.example.dto.EmailHistoryDTO;
import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.service.EmailHistoryService;
import com.example.utility.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/emailHistory")
public class EmailHistoryController {
    @Autowired
    private EmailHistoryService emailHistoryService;
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getByEmail")
    public ResponseEntity<List<EmailHistoryDTO>>getByEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(emailHistoryService.getEmailHistoryByEmail(email));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getByDate")
    public ResponseEntity<List<EmailHistoryDTO>>getByDate(@RequestParam("date")LocalDate date){
        return ResponseEntity.ok(emailHistoryService.getByDate(date));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<EmailHistoryDTO>>pagination(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                               @RequestParam(value = "size",defaultValue = "10") Integer size){
        return ResponseEntity.ok(emailHistoryService.pagination(page-1,size));
    }
}
