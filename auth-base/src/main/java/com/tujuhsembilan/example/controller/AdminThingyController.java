package com.tujuhsembilan.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;

@RestController
@PreAuthorize("hasAuthority('SYSTEM') or hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminThingyController {

   
    @PostMapping("/do-something")
    public ResponseEntity<?> doSomething() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Current user authorities: " + authentication.getAuthorities());
        return ResponseEntity.ok().build();
    }
}
