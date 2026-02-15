package com.chufesgesr.repositories;

import com.chufesgesr.entities.Specialite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {
    
    Optional<Specialite> findByNom(String nom);
    
    boolean existsByNom(String nom);
}
