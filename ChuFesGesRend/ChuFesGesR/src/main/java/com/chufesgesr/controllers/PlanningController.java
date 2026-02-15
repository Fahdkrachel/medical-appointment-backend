package com.chufesgesr.controllers;

import com.chufesgesr.entities.Planning;
import com.chufesgesr.repositories.PlanningRepository;
import com.chufesgesr.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/plannings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PlanningController {
    
    private final PlanningRepository planningRepository;
    private final UtilisateurRepository utilisateurRepository;
    
    @PostMapping
    @PreAuthorize("hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> createPlanning(@RequestBody Map<String, Object> request) {
        try {
            Long medecinId = Long.parseLong(request.get("medecinId").toString());
            String jourStr = request.get("jour").toString();
            Integer quota = Integer.parseInt(request.get("quota").toString());
            
            LocalDate jour = LocalDate.parse(jourStr);
            
            // Vérifier que le médecin existe
            if (!utilisateurRepository.existsById(medecinId)) {
                return ResponseEntity.badRequest().body("Médecin non trouvé");
            }
            
            // Vérifier que la date n'est pas dans le passé
            if (jour.isBefore(LocalDate.now())) {
                return ResponseEntity.badRequest().body("Impossible de créer un planning dans le passé");
            }
            
            // Vérifier qu'il n'y a pas déjà un planning pour ce médecin à cette date
            List<Planning> existingPlannings = planningRepository.findByMedecinIdAndJour(medecinId, jour);
            if (!existingPlannings.isEmpty()) {
                return ResponseEntity.badRequest().body("Un planning existe déjà pour ce médecin à cette date");
            }
            
            Planning planning = new Planning();
            planning.setJour(jour);
            planning.setQuota(quota);
            planning.setCompteur(0);
            
            // Récupérer le médecin et l'associer
            utilisateurRepository.findById(medecinId).ifPresent(medecin -> {
                planning.setMedecin((com.chufesgesr.entities.Medecin) medecin);
            });
            
            Planning savedPlanning = planningRepository.save(planning);
            return ResponseEntity.ok(savedPlanning);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création du planning: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> getPlanning(@PathVariable Long id) {
        try {
            Optional<Planning> planning = planningRepository.findById(id);
            if (planning.isPresent()) {
                return ResponseEntity.ok(planning.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération du planning: " + e.getMessage());
        }
    }
    
    @GetMapping("/medecin/{medecinId}")
    @PreAuthorize("hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> getPlanningsByMedecin(@PathVariable Long medecinId) {
        try {
            List<Planning> plannings = planningRepository.findByMedecinId(medecinId);
            return ResponseEntity.ok(plannings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des plannings: " + e.getMessage());
        }
    }
    
    @GetMapping("/medecin/{medecinId}/future")
    @PreAuthorize("hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> getFuturePlanningsByMedecin(@PathVariable Long medecinId) {
        try {
            List<Planning> plannings = planningRepository.findByMedecinIdAndJourAfter(medecinId, LocalDate.now());
            return ResponseEntity.ok(plannings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des plannings: " + e.getMessage());
        }
    }
    
    @GetMapping("/jour/{jour}")
    @PreAuthorize("hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> getPlanningsByDate(@PathVariable String jour) {
        try {
            LocalDate date = LocalDate.parse(jour);
            List<Planning> plannings = planningRepository.findByJour(date);
            return ResponseEntity.ok(plannings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des plannings: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> updatePlanning(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            Optional<Planning> planningOpt = planningRepository.findById(id);
            if (!planningOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Planning planning = planningOpt.get();
            
            // Mettre à jour le quota si fourni
            if (request.containsKey("quota")) {
                Integer newQuota = Integer.parseInt(request.get("quota").toString());
                if (newQuota < planning.getCompteur()) {
                    return ResponseEntity.badRequest().body("Le nouveau quota ne peut pas être inférieur au nombre de rendez-vous existants");
                }
                planning.setQuota(newQuota);
            }
            
            // Mettre à jour la date si fournie
            if (request.containsKey("jour")) {
                String jourStr = request.get("jour").toString();
                LocalDate newJour = LocalDate.parse(jourStr);
                
                if (newJour.isBefore(LocalDate.now())) {
                    return ResponseEntity.badRequest().body("Impossible de modifier la date vers le passé");
                }
                
                // Vérifier qu'il n'y a pas de conflit
                List<Planning> existingPlannings = planningRepository.findByMedecinIdAndJour(planning.getMedecin().getId(), newJour);
                if (!existingPlannings.isEmpty() && !existingPlannings.get(0).getId().equals(id)) {
                    return ResponseEntity.badRequest().body("Un planning existe déjà pour ce médecin à cette date");
                }
                
                planning.setJour(newJour);
            }
            
            Planning savedPlanning = planningRepository.save(planning);
            return ResponseEntity.ok(savedPlanning);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour du planning: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> deletePlanning(@PathVariable Long id) {
        try {
            Optional<Planning> planningOpt = planningRepository.findById(id);
            if (!planningOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Planning planning = planningOpt.get();
            
            // Vérifier qu'il n'y a pas de rendez-vous associés
            if (!planning.getRdvs().isEmpty()) {
                return ResponseEntity.badRequest().body("Impossible de supprimer un planning avec des rendez-vous associés");
            }
            
            planningRepository.deleteById(id);
            return ResponseEntity.ok("Planning supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la suppression du planning: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}/availability")
    @PreAuthorize("hasRole('PATIENT') or hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> checkPlanningAvailability(@PathVariable Long id) {
        try {
            Optional<Planning> planningOpt = planningRepository.findById(id);
            if (!planningOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Planning planning = planningOpt.get();
            
            Map<String, Object> availability = Map.of(
                "planningId", planning.getId(),
                "jour", planning.getJour(),
                "quota", planning.getQuota(),
                "compteur", planning.getCompteur(),
                "placesRestantes", planning.placesRestantes(),
                "disponible", planning.estDisponible()
            );
            
            return ResponseEntity.ok(availability);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la vérification de disponibilité: " + e.getMessage());
        }
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllPlannings() {
        try {
            List<Planning> plannings = planningRepository.findAll();
            return ResponseEntity.ok(plannings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des plannings: " + e.getMessage());
        }
    }
}



