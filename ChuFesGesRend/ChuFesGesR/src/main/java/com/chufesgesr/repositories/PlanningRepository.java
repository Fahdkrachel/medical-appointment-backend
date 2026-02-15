package com.chufesgesr.repositories;

import com.chufesgesr.entities.Planning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlanningRepository extends JpaRepository<Planning, Long> {
    
    List<Planning> findByMedecinId(Long medecinId);
    
    List<Planning> findByJour(LocalDate jour);
    
    @Query("SELECT p FROM Planning p WHERE p.medecin.id = :medecinId AND p.jour = :jour")
    List<Planning> findByMedecinIdAndJour(@Param("medecinId") Long medecinId, @Param("jour") LocalDate jour);
    
    @Query("SELECT p FROM Planning p WHERE p.medecin.id = :medecinId AND p.jour >= :date")
    List<Planning> findByMedecinIdAndJourAfter(@Param("medecinId") Long medecinId, @Param("date") LocalDate date);
}
