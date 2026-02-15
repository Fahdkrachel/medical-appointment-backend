package com.chufesgesr.controllers;

import com.chufesgesr.entities.Service;
import com.chufesgesr.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ServiceController {
    
    private final ServiceRepository serviceRepository;
    
    @PostMapping
    public ResponseEntity<?> createService(@RequestBody Map<String, String> request) {
        try {
            String nomService = request.get("nomService");
            String description = request.get("description");
            String specialiste = request.get("specialiste");
            
            if (nomService == null) {
                return ResponseEntity.badRequest().body("Nom du service est requis");
            }
            
            if (serviceRepository.existsByNomService(nomService)) {
                return ResponseEntity.badRequest().body("Un service avec ce nom existe déjà");
            }
            
            Service service = new Service();
            service.setNomService(nomService);
            service.setDescription(description);
            service.setSpecialiste(specialiste);
            
            Service savedService = serviceRepository.save(service);
            return ResponseEntity.ok(savedService);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création du service: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getService(@PathVariable Long id) {
        try {
            Optional<Service> service = serviceRepository.findById(id);
            if (service.isPresent()) {
                return ResponseEntity.ok(service.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération du service: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            Optional<Service> serviceOpt = serviceRepository.findById(id);
            if (!serviceOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Service service = serviceOpt.get();
            String nomService = request.get("nomService");
            String description = request.get("description");
            String specialiste = request.get("specialiste");
            
            if (nomService != null) {
                service.setNomService(nomService);
            }
            if (description != null) {
                service.setDescription(description);
            }
            if (specialiste != null) {
                service.setSpecialiste(specialiste);
            }
            
            Service updatedService = serviceRepository.save(service);
            return ResponseEntity.ok(updatedService);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour du service: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(@PathVariable Long id) {
        try {
            if (!serviceRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            
            serviceRepository.deleteById(id);
            return ResponseEntity.ok("Service supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la suppression du service: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getAllServices() {
        try {
            List<Service> services = serviceRepository.findAll();
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des services: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}/medecins")
    public ResponseEntity<?> getServiceMedecins(@PathVariable Long id) {
        try {
            Optional<Service> service = serviceRepository.findById(id);
            if (service.isPresent()) {
                return ResponseEntity.ok(service.get().getMedecins());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des médecins: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}/appointments")
    public ResponseEntity<?> getServiceAppointments(@PathVariable Long id) {
        try {
            Optional<Service> service = serviceRepository.findById(id);
            if (service.isPresent()) {
                return ResponseEntity.ok(service.get().getRendezVous());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des rendez-vous: " + e.getMessage());
        }
    }
}
