package com.chufesgesr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TestController {
    
    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("Endpoint public accessible à tous");
    }
    
    @GetMapping("/patient")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> patientEndpoint() {
        return ResponseEntity.ok("Endpoint patient - accessible uniquement aux patients");
    }
    
    @GetMapping("/medecin")
    @PreAuthorize("hasRole('MEDECIN')")
    public ResponseEntity<String> medecinEndpoint() {
        return ResponseEntity.ok("Endpoint médecin - accessible uniquement aux médecins");
    }
    
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminEndpoint() {
        return ResponseEntity.ok("Endpoint admin - accessible uniquement aux admins");
    }
    
    @GetMapping("/authenticated")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> authenticatedEndpoint() {
        return ResponseEntity.ok("Endpoint authentifié - accessible à tous les utilisateurs connectés");
    }
}

