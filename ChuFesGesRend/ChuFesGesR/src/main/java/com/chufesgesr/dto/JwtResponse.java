package com.chufesgesr.dto;

import com.chufesgesr.entities.Utilisateur;
import com.chufesgesr.entities.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private Long id;
    private String nomComplet;
    private String telephone;
    private String email;
    private UserRole role;
    private Long idMedecin;
    private Long idAdmin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String token;

    public static JwtResponse fromUtilisateur(Utilisateur utilisateur, String token) {
        JwtResponse response = new JwtResponse();
        response.setId(utilisateur.getId());
        response.setNomComplet(utilisateur.getNomComplet());
        response.setTelephone(utilisateur.getTelephone());
        response.setEmail(utilisateur.getEmail());
        response.setRole(utilisateur.getRole());
        response.setIdMedecin(utilisateur.getIdMedecin());
        response.setIdAdmin(utilisateur.getIdAdmin());
        response.setCreatedAt(utilisateur.getCreatedAt());
        response.setUpdatedAt(utilisateur.getUpdatedAt());
        response.setToken(token);
        return response;
    }
}

