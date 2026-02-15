package com.chufesgesr.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MEDECIN")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Medecin extends Utilisateur {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialite_id")
    private Specialite specialite;
    
    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Planning> plannings = new ArrayList<>();
    
    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> rendezVous = new ArrayList<>();
    
    // Constructeur pour créer un médecin
    public Medecin(String nomComplet, String telephone, String email, String motDePasse, Service service, Specialite specialite) {
        this.setNomComplet(nomComplet);
        this.setTelephone(telephone);
        this.setEmail(email);
        this.setMotDePasse(motDePasse);
        this.setRole(com.chufesgesr.entities.enums.UserRole.MEDECIN);
        this.setService(service);
        this.setSpecialite(specialite);
        this.setIdMedecin(genererIdMedecin());
    }
    
    // Méthodes métier spécifiques aux médecins
    public Planning creerPlanning(LocalDate jour, Integer quota) {
        Planning planning = new Planning();
        planning.setJour(jour);
        planning.setQuota(quota);
        planning.setMedecin(this);
        this.plannings.add(planning);
        return planning;
    }
    
    public void modifierPlanning(Planning planning) {
        // Logique de modification du planning
        // Cette méthode sera implémentée dans le service
    }
    
    public List<Appointment> consulterRendezVous() {
        return this.rendezVous;
    }
    
    public void accepterRendezVous(Appointment appointment) {
        appointment.changerStatus(com.chufesgesr.entities.enums.AppointmentStatus.ACCEPTE);
        appointment.notifierPatient();
    }
    
    public void refuserRendezVous(Appointment appointment) {
        appointment.changerStatus(com.chufesgesr.entities.enums.AppointmentStatus.REFUSE);
        appointment.notifierPatient();
    }
    
    public void reporterRendezVous(Appointment appointment, LocalDateTime nouvelleDate) {
        appointment.setScheduledAt(nouvelleDate);
        appointment.changerStatus(com.chufesgesr.entities.enums.AppointmentStatus.REPORTE);
        appointment.notifierPatient();
    }
    
    public void consulterProfilPatient(Patient patient) {
        // Logique de consultation du profil patient
        // Cette méthode sera implémentée dans le service
    }
    
    public void consulterPieceJointe(Attachment attachment) {
        // Logique de consultation des pièces jointes
        // Cette méthode sera implémentée dans le service
    }
    
    // Méthode utilitaire pour générer un ID médecin
    private Long genererIdMedecin() {
        return System.currentTimeMillis();
    }
}
