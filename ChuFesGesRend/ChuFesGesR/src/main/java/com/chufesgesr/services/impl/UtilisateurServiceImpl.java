package com.chufesgesr.services.impl;

import com.chufesgesr.dto.UtilisateurDTO;
import com.chufesgesr.entities.*;
import com.chufesgesr.entities.enums.UserRole;
import com.chufesgesr.repositories.ServiceRepository;
import com.chufesgesr.repositories.SpecialiteRepository;
import com.chufesgesr.repositories.UtilisateurRepository;
import com.chufesgesr.services.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final ServiceRepository serviceRepository;
    private final SpecialiteRepository specialiteRepository;

    @Override
    public UtilisateurDTO creerPatient(String nomComplet, String telephone) {
        if (existeParTelephone(telephone)) {
            throw new RuntimeException("Un utilisateur avec ce téléphone existe déjà");
        }

        Patient patient = new Patient(nomComplet, telephone);
        Utilisateur savedPatient = utilisateurRepository.save(patient);
        return convertirEnDTO(savedPatient);
    }

    @Override
    public UtilisateurDTO creerMedecin(String nomComplet, String telephone, String email, String motDePasse, Long serviceId, Long specialiteId) {
        if (existeParTelephone(telephone)) {
            throw new RuntimeException("Un utilisateur avec ce téléphone existe déjà");
        }
        if (existeParEmail(email)) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }

        com.chufesgesr.entities.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service non trouvé"));

        Specialite specialite = specialiteRepository.findById(specialiteId)
                .orElseThrow(() -> new RuntimeException("Spécialité non trouvée"));

        Medecin medecin = new Medecin(nomComplet, telephone, email, motDePasse, service, specialite);
        Utilisateur savedMedecin = utilisateurRepository.save(medecin);
        return convertirEnDTO(savedMedecin);
    }

    @Override
    public UtilisateurDTO creerAdmin(String nomComplet, String telephone, String email, String motDePasse) {
        if (existeParTelephone(telephone)) {
            throw new RuntimeException("Un utilisateur avec ce téléphone existe déjà");
        }
        if (existeParEmail(email)) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }

        Admin admin = new Admin(nomComplet, telephone, email, motDePasse);
        Utilisateur savedAdmin = utilisateurRepository.save(admin);
        return convertirEnDTO(savedAdmin);
    }

    @Override
    public Optional<UtilisateurDTO> seConnecter(String telephone, String motDePasse) {
        return utilisateurRepository.findByTelephone(telephone)
                .filter(utilisateur -> utilisateur.seConnecter(telephone, motDePasse))
                .map(this::convertirEnDTO);
    }

    @Override
    public Optional<Utilisateur> seConnecterEntity(String telephone, String motDePasse) {
        return utilisateurRepository.findByTelephone(telephone)
                .filter(utilisateur -> utilisateur.seConnecter(telephone, motDePasse));
    }

    @Override
    public UtilisateurDTO mettreAJourProfil(Long id, String nomComplet, String telephone, String email) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        utilisateur.setNomComplet(nomComplet);
        utilisateur.setTelephone(telephone);
        if (email != null && !email.isEmpty()) {
            utilisateur.setEmail(email);
        }

        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
        return convertirEnDTO(savedUtilisateur);
    }

    @Override
    public UtilisateurDTO changerMotDePasse(Long id, String ancienMotDePasse, String nouveauMotDePasse) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!utilisateur.getMotDePasse().equals(ancienMotDePasse)) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }

        utilisateur.setMotDePasse(nouveauMotDePasse);
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
        return convertirEnDTO(savedUtilisateur);
    }

    @Override
    public List<UtilisateurDTO> trouverParRole(UserRole role) {
        return utilisateurRepository.findByRole(role)
                .stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UtilisateurDTO> trouverTousLesPatients() {
        return utilisateurRepository.findPatients()
                .stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UtilisateurDTO> trouverTousLesMedecins() {
        return trouverParRole(UserRole.MEDECIN);
    }

    @Override
    public List<UtilisateurDTO> trouverTousLesAdmins() {
        return trouverParRole(UserRole.ADMIN);
    }

    @Override
    public Optional<UtilisateurDTO> trouverParId(Long id) {
        return utilisateurRepository.findById(id)
                .map(this::convertirEnDTO);
    }

    @Override
    public Optional<UtilisateurDTO> trouverParTelephone(String telephone) {
        return utilisateurRepository.findByTelephone(telephone)
                .map(this::convertirEnDTO);
    }

    @Override
    public void supprimerUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }

    @Override
    public boolean existeParTelephone(String telephone) {
        return utilisateurRepository.existsByTelephone(telephone);
    }

    @Override
    public boolean existeParEmail(String email) {
        return utilisateurRepository.existsByEmail(email);
    }

    private UtilisateurDTO convertirEnDTO(Utilisateur utilisateur) {
        return new UtilisateurDTO(
                utilisateur.getId(),
                utilisateur.getNomComplet(),
                utilisateur.getTelephone(),
                utilisateur.getEmail(),
                utilisateur.getRole(),
                utilisateur.getIdMedecin(),
                utilisateur.getIdAdmin(),
                utilisateur.getCreatedAt(),
                utilisateur.getUpdatedAt()
        );
    }
}