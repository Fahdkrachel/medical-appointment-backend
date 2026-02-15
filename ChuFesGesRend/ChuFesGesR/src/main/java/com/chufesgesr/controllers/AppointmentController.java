package com.chufesgesr.controllers;

import com.chufesgesr.dto.AppointmentDTO;
import com.chufesgesr.entities.enums.AppointmentStatus;
import com.chufesgesr.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AppointmentController {
    
    private final AppointmentService appointmentService;
    
    @PostMapping
    @PreAuthorize("hasRole('PATIENT') or hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> createAppointment(@RequestBody Map<String, Object> request) {
        try {
            Long patientId = Long.parseLong(request.get("patientId").toString());
            Long medecinId = Long.parseLong(request.get("medecinId").toString());
            Long serviceId = Long.parseLong(request.get("serviceId").toString());
            Long planningId = Long.parseLong(request.get("planningId").toString());
            LocalDateTime scheduledAt = LocalDateTime.parse(request.get("scheduledAt").toString());
            String note = (String) request.get("note");
            
            AppointmentDTO appointment = appointmentService.creerRendezVous(patientId, medecinId, serviceId, planningId, scheduledAt, note);
            return ResponseEntity.ok(appointment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création du rendez-vous: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT') or hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> getAppointment(@PathVariable Long id) {
        try {
            Optional<AppointmentDTO> appointment = appointmentService.trouverParId(id);
            if (appointment.isPresent()) {
                return ResponseEntity.ok(appointment.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération du rendez-vous: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> updateAppointmentStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            String raison = request.get("raison");
            String nouvelleDate = request.get("nouvelleDate");
            
            AppointmentDTO updatedAppointment = null;
            
            switch (status.toUpperCase()) {
                case "ACCEPTE":
                    updatedAppointment = appointmentService.accepterRendezVous(id);
                    break;
                case "REFUSE":
                    updatedAppointment = appointmentService.refuserRendezVous(id, raison);
                    break;
                case "REPORTE":
                    if (nouvelleDate != null) {
                        LocalDateTime date = LocalDateTime.parse(nouvelleDate);
                        updatedAppointment = appointmentService.reporterRendezVous(id, date);
                    } else {
                        return ResponseEntity.badRequest().body("Nouvelle date requise pour reporter un rendez-vous");
                    }
                    break;
                case "ANNULE":
                    updatedAppointment = appointmentService.annulerRendezVous(id);
                    break;
                default:
                    return ResponseEntity.badRequest().body("Statut invalide");
            }
            
            return ResponseEntity.ok(updatedAppointment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour du statut: " + e.getMessage());
        }
    }
    
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getAppointmentsByPatient(@PathVariable Long patientId) {
        try {
            List<AppointmentDTO> appointments = appointmentService.trouverParPatient(patientId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des rendez-vous: " + e.getMessage());
        }
    }
    
    @GetMapping("/medecin/{medecinId}")
    @PreAuthorize("hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> getAppointmentsByMedecin(@PathVariable Long medecinId) {
        try {
            List<AppointmentDTO> appointments = appointmentService.trouverParMedecin(medecinId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des rendez-vous: " + e.getMessage());
        }
    }
    
    @GetMapping("/service/{serviceId}")
    @PreAuthorize("hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> getAppointmentsByService(@PathVariable Long serviceId) {
        try {
            List<AppointmentDTO> appointments = appointmentService.trouverParService(serviceId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des rendez-vous: " + e.getMessage());
        }
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> getAppointmentsByStatus(@PathVariable String status) {
        try {
            AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());
            List<AppointmentDTO> appointments = appointmentService.trouverParStatus(appointmentStatus);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des rendez-vous: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.supprimerRendezVous(id);
            return ResponseEntity.ok("Rendez-vous supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la suppression du rendez-vous: " + e.getMessage());
        }
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllAppointments() {
        try {
            List<AppointmentDTO> appointments = appointmentService.trouverTousLesRendezVous();
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des rendez-vous: " + e.getMessage());
        }
    }
}
