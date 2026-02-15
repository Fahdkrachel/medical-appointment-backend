package com.chufesgesr.services.impl;

import com.chufesgesr.dto.AppointmentDTO;
import com.chufesgesr.dto.AttachmentDTO;
import com.chufesgesr.entities.*;
import com.chufesgesr.entities.enums.AppointmentStatus;
import com.chufesgesr.repositories.*;
import com.chufesgesr.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ServiceRepository serviceRepository;
    private final PlanningRepository planningRepository;

    @Override
    public AppointmentDTO creerRendezVous(Long patientId, Long medecinId, Long serviceId, Long planningId,
                                          LocalDateTime scheduledAt, String note) {
        // Vérifier que le patient existe
        Utilisateur patient = utilisateurRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient non trouvé"));

        // Vérifier que le médecin existe
        Utilisateur medecin = utilisateurRepository.findById(medecinId)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));

        // Vérifier que le service existe
        com.chufesgesr.entities.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service non trouvé"));

        // Vérifier que le planning existe
        Planning planning = planningRepository.findById(planningId)
                .orElseThrow(() -> new RuntimeException("Planning non trouvé"));

        // Vérifier que la date n'est pas dans le passé
        if (scheduledAt.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Impossible de créer un rendez-vous dans le passé");
        }

        // Créer le rendez-vous
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setMedecin(medecin);
        appointment.setService(service);
        appointment.setPlanning(planning);
        appointment.setScheduledAt(scheduledAt);
        appointment.setNote(note);
        appointment.setStatus(AppointmentStatus.EN_ATTENTE);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertirEnDTO(savedAppointment);
    }

    @Override
    public AppointmentDTO accepterRendezVous(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Rendez-vous non trouvé"));

        if (!appointment.estEnAttente()) {
            throw new RuntimeException("Seuls les rendez-vous en attente peuvent être acceptés");
        }

        appointment.changerStatus(AppointmentStatus.ACCEPTE);
        appointment.notifierPatient();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertirEnDTO(savedAppointment);
    }

    @Override
    public AppointmentDTO refuserRendezVous(Long appointmentId, String raison) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Rendez-vous non trouvé"));

        if (!appointment.estEnAttente()) {
            throw new RuntimeException("Seuls les rendez-vous en attente peuvent être refusés");
        }

        appointment.changerStatus(AppointmentStatus.REFUSE);
        if (raison != null && !raison.trim().isEmpty()) {
            appointment.setNote(appointment.getNote() != null ?
                    appointment.getNote() + "\nRaison du refus: " + raison :
                    "Raison du refus: " + raison);
        }
        appointment.notifierPatient();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertirEnDTO(savedAppointment);
    }

    @Override
    public AppointmentDTO reporterRendezVous(Long appointmentId, LocalDateTime nouvelleDate) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Rendez-vous non trouvé"));

        if (appointment.estAnnule()) {
            throw new RuntimeException("Impossible de reporter un rendez-vous annulé");
        }

        if (nouvelleDate.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Impossible de reporter un rendez-vous dans le passé");
        }

        appointment.setScheduledAt(nouvelleDate);
        appointment.changerStatus(AppointmentStatus.REPORTE);
        appointment.notifierPatient();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertirEnDTO(savedAppointment);
    }

    @Override
    public AppointmentDTO annulerRendezVous(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Rendez-vous non trouvé"));

        if (appointment.estAnnule()) {
            throw new RuntimeException("Le rendez-vous est déjà annulé");
        }

        appointment.changerStatus(AppointmentStatus.ANNULE);
        appointment.notifierPatient();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertirEnDTO(savedAppointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> trouverParPatient(Long patientId) {
        if (!utilisateurRepository.existsById(patientId)) {
            throw new RuntimeException("Patient non trouvé");
        }

        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> trouverParMedecin(Long medecinId) {
        if (!utilisateurRepository.existsById(medecinId)) {
            throw new RuntimeException("Médecin non trouvé");
        }

        return appointmentRepository.findByMedecinId(medecinId)
                .stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> trouverParService(Long serviceId) {
        if (!serviceRepository.existsById(serviceId)) {
            throw new RuntimeException("Service non trouvé");
        }

        return appointmentRepository.findByServiceId(serviceId)
                .stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> trouverParStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status)
                .stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> trouverParPatientEtStatus(Long patientId, AppointmentStatus status) {
        if (!utilisateurRepository.existsById(patientId)) {
            throw new RuntimeException("Patient non trouvé");
        }

        return appointmentRepository.findByPatientIdAndStatus(patientId, status)
                .stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> trouverParMedecinEtStatus(Long medecinId, AppointmentStatus status) {
        if (!utilisateurRepository.existsById(medecinId)) {
            throw new RuntimeException("Médecin non trouvé");
        }

        return appointmentRepository.findByMedecinIdAndStatus(medecinId, status)
                .stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> trouverParPeriode(LocalDateTime debut, LocalDateTime fin) {
        if (debut.isAfter(fin)) {
            throw new RuntimeException("La date de début doit être antérieure à la date de fin");
        }

        return appointmentRepository.findByScheduledAtBetween(debut, fin)
                .stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppointmentDTO> trouverParId(Long id) {
        return appointmentRepository.findById(id)
                .map(this::convertirEnDTO);
    }

    @Override
    public void supprimerRendezVous(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Rendez-vous non trouvé");
        }

        appointmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> trouverTousLesRendezVous() {
        return appointmentRepository.findAll()
                .stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une entité Appointment en DTO
     */
    private AppointmentDTO convertirEnDTO(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        // Convertir les attachments
        List<AttachmentDTO> attachmentDTOs = appointment.getAttachments()
                .stream()
                .map(this::convertirAttachmentEnDTO)
                .collect(Collectors.toList());

        return new AppointmentDTO(
                appointment.getId(),
                appointment.getStatus(),
                appointment.getScheduledAt(),
                appointment.getNote(),
                appointment.getCreatedAt(),
                appointment.getUpdatedAt(),
                appointment.getPatient().getId(),
                appointment.getPatient().getNomComplet(),
                appointment.getMedecin().getId(),
                appointment.getMedecin().getNomComplet(),
                appointment.getService().getIdService(),
                appointment.getService().getNomService(),
                appointment.getPlanning().getId(),
                attachmentDTOs
        );
    }

    /**
     * Convertit une entité Attachment en DTO
     */
    private AttachmentDTO convertirAttachmentEnDTO(Attachment attachment) {
        if (attachment == null) {
            return null;
        }

        return new AttachmentDTO(
                attachment.getId(),
                attachment.getFilePath(),
                attachment.getDescription(),
                attachment.getCreatedAt(),
                attachment.getUpdatedAt()
        );
    }
}