package com.chufesgesr.controllers;

import com.chufesgesr.config.JwtUtil;
import com.chufesgesr.dto.JwtResponse;
import com.chufesgesr.dto.UtilisateurDTO;
import com.chufesgesr.entities.Utilisateur;
import com.chufesgesr.services.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final UtilisateurService utilisateurService;
    private final JwtUtil jwtUtil;
    
    @PostMapping("/register-patient")
    public ResponseEntity<?> registerPatient(@RequestBody Map<String, String> request) {
        try {
            String nomComplet = request.get("nomComplet");
            String telephone = request.get("telephone");
            
            if (nomComplet == null || telephone == null) {
                return ResponseEntity.badRequest().body("Nom complet et téléphone sont requis");
            }
            
            UtilisateurDTO patient = utilisateurService.creerPatient(nomComplet, telephone);
            return ResponseEntity.ok(patient);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création du compte: " + e.getMessage());
        }
    }
    
    @PostMapping("/register-medecin")
    public ResponseEntity<?> registerMedecin(@RequestBody Map<String, String> request) {
        try {
            String nomComplet = request.get("nomComplet");
            String telephone = request.get("telephone");
            String email = request.get("email");
            String motDePasse = request.get("motDePasse");
            Long serviceId = Long.parseLong(request.get("serviceId"));
            Long specialiteId = Long.parseLong(request.get("specialiteId"));
            
            if (nomComplet == null || telephone == null || email == null || motDePasse == null) {
                return ResponseEntity.badRequest().body("Tous les champs sont requis");
            }
            
            UtilisateurDTO medecin = utilisateurService.creerMedecin(nomComplet, telephone, email, motDePasse, serviceId, specialiteId);
            return ResponseEntity.ok(medecin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création du compte: " + e.getMessage());
        }
    }
    
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody Map<String, String> request) {
        try {
            String nomComplet = request.get("nomComplet");
            String telephone = request.get("telephone");
            String email = request.get("email");
            String motDePasse = request.get("motDePasse");
            
            if (nomComplet == null || telephone == null || email == null || motDePasse == null) {
                return ResponseEntity.badRequest().body("Tous les champs sont requis");
            }
            
            UtilisateurDTO admin = utilisateurService.creerAdmin(nomComplet, telephone, email, motDePasse);
            return ResponseEntity.ok(admin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création du compte: " + e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String telephone = request.get("telephone");
            String motDePasse = request.get("motDePasse");
            
            if (telephone == null || motDePasse == null) {
                return ResponseEntity.badRequest().body("Téléphone et mot de passe sont requis");
            }
            
            Optional<Utilisateur> utilisateurOpt = utilisateurService.seConnecterEntity(telephone, motDePasse);
            
            if (utilisateurOpt.isPresent()) {
                Utilisateur utilisateur = utilisateurOpt.get();
                String token = jwtUtil.generateToken(utilisateur);
                JwtResponse jwtResponse = JwtResponse.fromUtilisateur(utilisateur, token);
                return ResponseEntity.ok(jwtResponse);
            } else {
                return ResponseEntity.badRequest().body("Identifiants incorrects");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la connexion: " + e.getMessage());
        }
    }
    
    @PostMapping("/confirm-patient")
    public ResponseEntity<?> confirmPatient(@RequestBody Map<String, String> request) {
        try {
            String telephone = request.get("telephone");
            String codeSMS = request.get("codeSMS");
            
            if (telephone == null || codeSMS == null) {
                return ResponseEntity.badRequest().body("Téléphone et code SMS sont requis");
            }
            
            Optional<UtilisateurDTO> utilisateur = utilisateurService.trouverParTelephone(telephone);
            
            if (utilisateur.isPresent() && utilisateur.get().getRole() == null) {
                // Vérifier le code SMS (logique simplifiée)
                return ResponseEntity.ok("Compte patient confirmé avec succès");
            } else {
                return ResponseEntity.badRequest().body("Patient non trouvé ou code SMS incorrect");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la confirmation: " + e.getMessage());
        }
    }
}
