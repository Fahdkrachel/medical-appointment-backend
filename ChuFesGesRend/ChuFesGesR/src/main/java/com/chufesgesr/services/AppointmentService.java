package com.chufesgesr.services;

import com.chufesgesr.dto.AppointmentDTO;
import com.chufesgesr.entities.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    
    AppointmentDTO creerRendezVous(Long patientId, Long medecinId, Long serviceId, Long planningId, LocalDateTime scheduledAt, String note);
    
    AppointmentDTO accepterRendezVous(Long appointmentId);
    
    AppointmentDTO refuserRendezVous(Long appointmentId, String raison);
    
    AppointmentDTO reporterRendezVous(Long appointmentId, LocalDateTime nouvelleDate);
    
    AppointmentDTO annulerRendezVous(Long appointmentId);
    
    List<AppointmentDTO> trouverParPatient(Long patientId);
    
    List<AppointmentDTO> trouverParMedecin(Long medecinId);
    
    List<AppointmentDTO> trouverParService(Long serviceId);
    
    List<AppointmentDTO> trouverParStatus(AppointmentStatus status);
    
    List<AppointmentDTO> trouverParPatientEtStatus(Long patientId, AppointmentStatus status);
    
    List<AppointmentDTO> trouverParMedecinEtStatus(Long medecinId, AppointmentStatus status);
    
    List<AppointmentDTO> trouverParPeriode(LocalDateTime debut, LocalDateTime fin);
    
    Optional<AppointmentDTO> trouverParId(Long id);
    
    void supprimerRendezVous(Long id);
    
    List<AppointmentDTO> trouverTousLesRendezVous();
}
