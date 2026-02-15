package com.chufesgesr.entities;

import com.chufesgesr.entities.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status = AppointmentStatus.EN_ATTENTE;
    
    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;
    
    @Column(name = "note")
    private String note;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Utilisateur patient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medecin_id", nullable = false)
    private Utilisateur medecin;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planning_id", nullable = false)
    private Planning planning;
    
    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attachment> attachments = new ArrayList<>();
    
    // Méthodes métier
    public boolean estEnAttente() {
        return AppointmentStatus.EN_ATTENTE.equals(this.status);
    }
    
    public boolean estConfirme() {
        return AppointmentStatus.ACCEPTE.equals(this.status);
    }
    
    public boolean estAnnule() {
        return AppointmentStatus.ANNULE.equals(this.status);
    }
    
    public void changerStatus(AppointmentStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void notifierPatient() {
        // Logique de notification du patient
        // Créer une notification pour le patient
    }
    
    public void ajouterAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        attachment.setAppointment(this);
    }
    
    public void supprimerAttachment(Attachment attachment) {
        this.attachments.remove(attachment);
        attachment.setAppointment(null);
    }
}
