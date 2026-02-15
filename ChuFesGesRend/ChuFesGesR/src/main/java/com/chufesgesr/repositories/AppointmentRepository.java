package com.chufesgesr.repositories;

import com.chufesgesr.entities.Appointment;
import com.chufesgesr.entities.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByPatientId(Long patientId);
    
    List<Appointment> findByMedecinId(Long medecinId);

    @Query("SELECT a FROM Appointment a WHERE a.service.idService = :serviceId")
    List<Appointment> findByServiceId(@Param("serviceId") Long serviceId);
    
    List<Appointment> findByStatus(AppointmentStatus status);
    
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.status = :status")
    List<Appointment> findByPatientIdAndStatus(@Param("patientId") Long patientId, @Param("status") AppointmentStatus status);
    
    @Query("SELECT a FROM Appointment a WHERE a.medecin.id = :medecinId AND a.status = :status")
    List<Appointment> findByMedecinIdAndStatus(@Param("medecinId") Long medecinId, @Param("status") AppointmentStatus status);
    
    @Query("SELECT a FROM Appointment a WHERE a.scheduledAt BETWEEN :startDate AND :endDate")
    List<Appointment> findByScheduledAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
