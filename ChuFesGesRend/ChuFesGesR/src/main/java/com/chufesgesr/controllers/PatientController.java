package com.chufesgesr.controllers;

import com.chufesgesr.dto.AppointmentDTO;
import com.chufesgesr.dto.UtilisateurDTO;
import com.chufesgesr.services.AppointmentService;
import com.chufesgesr.services.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PatientController {
    
    private final UtilisateurService utilisateurService;
    private final AppointmentService appointmentService;
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getPatient(@PathVariable Long id) {
        try {
            Optional<UtilisateurDTO> patient = utilisateurService.trouverParId(id);
            if (patient.isPresent() && patient.get().getRole() == null) {
                return ResponseEntity.ok(patient.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération du patient: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}/appointments")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getPatientAppointments(@PathVariable Long id) {
        try {
            List<AppointmentDTO> appointments = appointmentService.trouverParPatient(id);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des rendez-vous: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/profile")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> updatePatientProfile(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String nomComplet = request.get("nomComplet");
            String telephone = request.get("telephone");
            
            if (nomComplet == null || telephone == null) {
                return ResponseEntity.badRequest().body("Nom complet et téléphone sont requis");
            }
            
            UtilisateurDTO updatedPatient = utilisateurService.mettreAJourProfil(id, nomComplet, telephone, null);
            return ResponseEntity.ok(updatedPatient);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour du profil: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/password")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> changePatientPassword(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String ancienMotDePasse = request.get("ancienMotDePasse");
            String nouveauMotDePasse = request.get("nouveauMotDePasse");
            
            if (ancienMotDePasse == null || nouveauMotDePasse == null) {
                return ResponseEntity.badRequest().body("Ancien et nouveau mot de passe sont requis");
            }
            
            UtilisateurDTO updatedPatient = utilisateurService.changerMotDePasse(id, ancienMotDePasse, nouveauMotDePasse);
            return ResponseEntity.ok(updatedPatient);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors du changement de mot de passe: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}/notifications")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getPatientNotifications(@PathVariable Long id) {
        try {
            // Cette méthode sera implémentée avec le service de notifications
            return ResponseEntity.ok("Notifications du patient " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des notifications: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}/appointments/history")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getPatientAppointmentHistory(@PathVariable Long id) {
        try {
            List<AppointmentDTO> appointments = appointmentService.trouverParPatient(id);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération de l'historique: " + e.getMessage());
        }
    }
}
