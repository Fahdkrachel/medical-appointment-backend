package com.chufesgesr.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("PATIENT")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends Utilisateur {
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> mesRendezVous = new ArrayList<>();
    
    // Constructeur pour créer un patient
    public Patient(String nomComplet, String telephone) {
        this.setNomComplet(nomComplet);
        this.setTelephone(telephone);
        this.setEmail(null); // Les patients n'ont pas d'email
        this.setRole(null); // null pour les patients
        this.setMotDePasse(genererCodeSMS()); // Code SMS généré
    }
    
    // Méthodes métier spécifiques aux patients
    public static Utilisateur creerCompte(String nom, String telephone) {
        Patient patient = new Patient(nom, telephone);
        return patient;
    }
    
    public boolean confirmerCompte(String codeSMS) {
        return this.getMotDePasse().equals(codeSMS);
    }
    
    public Appointment prendreRendezVous(Service service, java.time.LocalDate date) {
        // Logique de prise de rendez-vous
        // Cette méthode sera implémentée dans le service
        return null;
    }
    
    public List<Appointment> consulterMesRendezVous() {
        return this.mesRendezVous;
    }
    
    public void joindreFichier(Appointment appointment, String filePath) {
        Attachment attachment = new Attachment();
        attachment.setFilePath(filePath);
        attachment.setAppointment(appointment);
        appointment.ajouterAttachment(attachment);
    }
    
    // Méthode utilitaire pour générer un code SMS
    private String genererCodeSMS() {
        // Génération d'un code SMS à 6 chiffres
        return String.format("%06d", (int)(Math.random() * 1000000));
    }
}
