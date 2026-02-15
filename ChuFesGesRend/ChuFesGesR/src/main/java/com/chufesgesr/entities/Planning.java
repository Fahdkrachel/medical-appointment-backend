package com.chufesgesr.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plannings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Planning {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "jour", nullable = false)
    private LocalDate jour;
    
    @Column(name = "quota", nullable = false)
    private Integer quota;
    
    @Column(name = "compteur", nullable = false)
    private Integer compteur = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medecin_id", nullable = false)
    private Medecin medecin;
    
    @OneToMany(mappedBy = "planning", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> rdvs = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Méthodes métier
    public void ajouterQuota(Integer val) {
        this.quota += val;
    }
    
    public void retirerQuota(Integer val) {
        if (this.quota >= val) {
            this.quota -= val;
        }
    }
    
    public Integer placesRestantes() {
        return this.quota - this.compteur;
    }
    
    public boolean estDisponible() {
        return this.compteur < this.quota;
    }
    
    public void incrementerCompteur() {
        this.compteur++;
    }
    
    public void decrementerCompteur() {
        if (this.compteur > 0) {
            this.compteur--;
        }
    }
}
