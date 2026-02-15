package com.chufesgesr.repositories;

import com.chufesgesr.entities.Utilisateur;
import com.chufesgesr.entities.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    
    Optional<Utilisateur> findByTelephone(String telephone);
    
    Optional<Utilisateur> findByEmail(String email);
    
    List<Utilisateur> findByRole(UserRole role);
    
    @Query("SELECT u FROM Utilisateur u WHERE u.role IS NULL")
    List<Utilisateur> findPatients();
    
    @Query("SELECT u FROM Utilisateur u WHERE u.role = :role")
    List<Utilisateur> findByRoleType(@Param("role") UserRole role);
    
    boolean existsByTelephone(String telephone);
    
    boolean existsByEmail(String email);
}
