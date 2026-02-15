package com.chufesgesr.entities;

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
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_service")
    private Long idService;
    
    @Column(name = "nom_service", nullable = false, unique = true)
    private String nomService;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "specialiste")
    private String specialiste; // nom/texte de la spécialité pour ce service
    
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Medecin> medecins = new ArrayList<>();
    
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> rendezVous = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Méthodes métier
    public void ajouterMedecin(Medecin medecin) {
        this.medecins.add(medecin);
        medecin.setService(this);
    }
    
    public void supprimerMedecin(Medecin medecin) {
        this.medecins.remove(medecin);
        medecin.setService(null);
    }
    
    public void ajouterRendezVous(Appointment rendezVous) {
        this.rendezVous.add(rendezVous);
        rendezVous.setService(this);
    }
    
    public List<Appointment> consulterRendezVous() {
        return this.rendezVous;
    }
}
