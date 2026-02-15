package com.chufesgesr.entities;

import com.chufesgesr.entities.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;
    
    @Column(name = "message", nullable = false)
    private String message;
    
    @Column(name = "read_status", nullable = false)
    private Boolean readStatus = false;
    
    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinataire_id", nullable = true)
    private Utilisateur destinataire;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Méthodes métier
    public void envoyerNotification() {
        this.dateCreation = LocalDateTime.now();
        // Logique d'envoi de notification selon le type
        switch (this.type) {
            case EMAIL:
                // Envoyer par email
                break;
            case SMS:
                // Envoyer par SMS
                break;
            case APP:
                // Notification dans l'application
                break;
            case GLOBAL:
                // Notification globale
                break;
        }
    }
    
    public void marquerCommeLue() {
        this.readStatus = true;
    }
    
    @PrePersist
    public void prePersist() {
        if (this.dateCreation == null) {
            this.dateCreation = LocalDateTime.now();
        }
    }
}
