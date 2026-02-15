package com.chufesgesr.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("ADMIN")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends Utilisateur {

    // CORRIGÉ: Les médecins ne sont pas mappés par "service" mais par "admin"
    // Si vous voulez que l'admin gère des médecins, utilisez @JoinColumn
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private List<Medecin> managedMedecins = new ArrayList<>();

    // CORRIGÉ: Les patients ne sont pas mappés par "patient" mais par "admin"
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private List<Patient> managedPatients = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private List<com.chufesgesr.entities.Service> managedServices = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private List<Appointment> supervisedAppointments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private List<Notification> sentNotifications = new ArrayList<>();

    // Constructeur pour créer un admin
    public Admin(String nomComplet, String telephone, String email, String motDePasse) {
        this.setNomComplet(nomComplet);
        this.setTelephone(telephone);
        this.setEmail(email);
        this.setMotDePasse(motDePasse);
        this.setRole(com.chufesgesr.entities.enums.UserRole.ADMIN);
        this.setIdAdmin(genererIdAdmin());
    }

    // Méthodes métier spécifiques aux admins
    public void ajouterMedecin(Medecin medecin) {
        this.managedMedecins.add(medecin);
    }

    public void modifierMedecin(Medecin medecin) {
        // Logique de modification du médecin
        // Cette méthode sera implémentée dans le service
    }

    public void supprimerMedecin(Medecin medecin) {
        this.managedMedecins.remove(medecin);
    }

    public List<Patient> consulterPatients() {
        return this.managedPatients;
    }

    public void supprimerPatient(Patient patient) {
        this.managedPatients.remove(patient);
    }

    public void bloquerPatient(Patient patient) {
        // Logique de blocage du patient
        // Cette méthode sera implémentée dans le service
    }

    public List<Appointment> voirTousRendezVous() {
        return this.supervisedAppointments;
    }

    public void annulerRendezVous(Appointment appointment) {
        appointment.changerStatus(com.chufesgesr.entities.enums.AppointmentStatus.ANNULE);
        appointment.notifierPatient();
    }

    public com.chufesgesr.entities.Service creerService(String nom, String desc) {
        com.chufesgesr.entities.Service service = new com.chufesgesr.entities.Service();
        service.setNomService(nom);
        service.setDescription(desc);
        this.managedServices.add(service);
        return service;
    }

    public void modifierService(com.chufesgesr.entities.Service service) {
        // Logique de modification du service
        // Cette méthode sera implémentée dans le service
    }

    public void supprimerService(com.chufesgesr.entities.Service service) {
        this.managedServices.remove(service);
    }

    public void envoyerNotificationGlobale(String msg, String cible) {
        Notification notification = new Notification();
        notification.setType(com.chufesgesr.entities.enums.NotificationType.GLOBAL);
        notification.setMessage(msg);
        notification.setDateCreation(java.time.LocalDateTime.now());
        this.sentNotifications.add(notification);
    }

    public void genererRapportStatistiques() {
        // Logique de génération de rapports statistiques
        // Cette méthode sera implémentée dans le service
    }

    // Méthode utilitaire pour générer un ID admin
    private Long genererIdAdmin() {
        return System.currentTimeMillis();
    }
}