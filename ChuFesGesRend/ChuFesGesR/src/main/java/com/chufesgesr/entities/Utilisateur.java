package com.chufesgesr.entities;

import com.chufesgesr.entities.enums.UserRole;
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
@Table(name = "utilisateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class Utilisateur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom_complet", nullable = false)
    private String nomComplet;
    
    @Column(name = "telephone", nullable = false, unique = true)
    private String telephone;
    
    @Column(name = "email")
    private String email; // null pour les patients
    
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse; // code SMS pour patients, mot de passe classique pour autres
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role; // null pour patients, MEDECIN/ADMIN pour autres
    
    @Column(name = "id_medecin")
    private Long idMedecin; // identifiant global pour médecins
    
    @Column(name = "id_admin")
    private Long idAdmin; // identifiant global pour admins
    
    @OneToMany(mappedBy = "destinataire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Méthodes métier
    public boolean seConnecter(String telephone, String motDePasse) {
        return this.telephone.equals(telephone) && this.motDePasse.equals(motDePasse);
    }
    
    public void deconnecter() {
        // Logique de déconnexion
    }
    
    public void mettreAJourProfil() {
        // Logique de mise à jour du profil
    }
    
    public void recevoirNotification(Notification notification) {
        this.notifications.add(notification);
        notification.setDestinataire(this);
    }
    
    // Méthodes utilitaires pour déterminer le type d'utilisateur
    public boolean estPatient() {
        return this.role == null;
    }
    
    public boolean estMedecin() {
        return UserRole.MEDECIN.equals(this.role);
    }
    
    public boolean estAdmin() {
        return UserRole.ADMIN.equals(this.role);
    }
}
