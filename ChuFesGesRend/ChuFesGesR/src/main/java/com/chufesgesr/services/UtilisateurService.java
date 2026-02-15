package com.chufesgesr.services;

import com.chufesgesr.dto.UtilisateurDTO;
import com.chufesgesr.entities.Utilisateur;
import com.chufesgesr.entities.enums.UserRole;

import java.util.List;
import java.util.Optional;

public interface UtilisateurService {
    
    UtilisateurDTO creerPatient(String nomComplet, String telephone);
    
    UtilisateurDTO creerMedecin(String nomComplet, String telephone, String email, String motDePasse, Long serviceId, Long specialiteId);
    
    UtilisateurDTO creerAdmin(String nomComplet, String telephone, String email, String motDePasse);
    
    Optional<UtilisateurDTO> seConnecter(String telephone, String motDePasse);
    
    Optional<Utilisateur> seConnecterEntity(String telephone, String motDePasse);
    
    UtilisateurDTO mettreAJourProfil(Long id, String nomComplet, String telephone, String email);
    
    UtilisateurDTO changerMotDePasse(Long id, String ancienMotDePasse, String nouveauMotDePasse);
    
    List<UtilisateurDTO> trouverParRole(UserRole role);
    
    List<UtilisateurDTO> trouverTousLesPatients();
    
    List<UtilisateurDTO> trouverTousLesMedecins();
    
    List<UtilisateurDTO> trouverTousLesAdmins();
    
    Optional<UtilisateurDTO> trouverParId(Long id);
    
    Optional<UtilisateurDTO> trouverParTelephone(String telephone);
    
    void supprimerUtilisateur(Long id);
    
    boolean existeParTelephone(String telephone);
    
    boolean existeParEmail(String email);
}
