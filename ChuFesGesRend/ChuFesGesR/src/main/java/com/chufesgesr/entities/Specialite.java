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
@Table(name = "specialites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Specialite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom", nullable = false, unique = true)
    private String nom;
    
    @Column(name = "description")
    private String description;
    
    @OneToMany(mappedBy = "specialite", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Medecin> medecins = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Méthodes métier
    public void ajouterSpecialite() {
        // Logique d'ajout de spécialité
    }
    
    public void modifierSpecialite() {
        // Logique de modification de spécialité
    }
    
    public void supprimerSpecialite() {
        // Logique de suppression de spécialité
    }
}
