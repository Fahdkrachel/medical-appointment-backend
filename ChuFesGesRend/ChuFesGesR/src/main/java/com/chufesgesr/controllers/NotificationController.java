package com.chufesgesr.controllers;

import com.chufesgesr.entities.Notification;
import com.chufesgesr.entities.enums.NotificationType;
import com.chufesgesr.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {
    
    private final NotificationRepository notificationRepository;
    
    @PostMapping("/global")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> sendGlobalNotification(@RequestBody Map<String, String> request) {
        try {
            String message = request.get("message");
            String cible = request.get("cible");
            
            if (message == null || message.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Message requis");
            }
            
            Notification notification = new Notification();
            notification.setType(NotificationType.GLOBAL);
            notification.setMessage(message);
            notification.setDateCreation(LocalDateTime.now());
            notification.setReadStatus(false);
            // Pour les notifications globales, destinataire est null
            notification.setDestinataire(null);
            
            Notification savedNotification = notificationRepository.save(notification);
            return ResponseEntity.ok(savedNotification);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'envoi de la notification: " + e.getMessage());
        }
    }
    
    @PostMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDECIN')")
    public ResponseEntity<?> sendUserNotification(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        try {
            String message = request.get("message");
            String type = request.get("type");
            
            if (message == null || message.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Message requis");
            }
            
            NotificationType notificationType = NotificationType.APP; // Par défaut
            if (type != null) {
                try {
                    notificationType = NotificationType.valueOf(type.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Type de notification invalide. Types disponibles: EMAIL, SMS, APP, GLOBAL");
                }
            }
            
            Notification notification = new Notification();
            notification.setType(notificationType);
            notification.setMessage(message);
            notification.setDateCreation(LocalDateTime.now());
            notification.setReadStatus(false);
            
            // Note: Dans une implémentation complète, on récupérerait l'utilisateur par son ID
            // et on l'associerait à la notification. Pour l'instant, on laisse destinataire null
            // car l'utilisateur sera associé via l'ID dans le repository
            
            Notification savedNotification = notificationRepository.save(notification);
            return ResponseEntity.ok(savedNotification);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'envoi de la notification: " + e.getMessage());
        }
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('PATIENT') or hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserNotifications(@PathVariable Long userId) {
        try {
            List<Notification> notifications = notificationRepository.findByDestinataireId(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des notifications: " + e.getMessage());
        }
    }
    
    @GetMapping("/user/{userId}/unread")
    @PreAuthorize("hasRole('PATIENT') or hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserUnreadNotifications(@PathVariable Long userId) {
        try {
            List<Notification> notifications = notificationRepository.findByDestinataireIdAndReadStatus(userId, false);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des notifications: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/read")
    @PreAuthorize("hasRole('PATIENT') or hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long id) {
        try {
            Optional<Notification> notificationOpt = notificationRepository.findById(id);
            if (!notificationOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Notification notification = notificationOpt.get();
            notification.marquerCommeLue();
            Notification savedNotification = notificationRepository.save(notification);
            return ResponseEntity.ok(savedNotification);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour de la notification: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT') or hasRole('MEDECIN') or hasRole('ADMIN')")
    public ResponseEntity<?> getNotification(@PathVariable Long id) {
        try {
            Optional<Notification> notification = notificationRepository.findById(id);
            if (notification.isPresent()) {
                return ResponseEntity.ok(notification.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération de la notification: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        try {
            if (!notificationRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            
            notificationRepository.deleteById(id);
            return ResponseEntity.ok("Notification supprimée avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la suppression de la notification: " + e.getMessage());
        }
    }
    
    @GetMapping("/type/{type}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getNotificationsByType(@PathVariable String type) {
        try {
            NotificationType notificationType = NotificationType.valueOf(type.toUpperCase());
            List<Notification> notifications = notificationRepository.findByType(notificationType);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des notifications: " + e.getMessage());
        }
    }
}
