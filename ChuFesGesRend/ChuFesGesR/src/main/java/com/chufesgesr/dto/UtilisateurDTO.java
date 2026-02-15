package com.chufesgesr.dto;

import com.chufesgesr.entities.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDTO {
    private Long id;
    private String nomComplet;
    private String telephone;
    private String email;
    private UserRole role;
    private Long idMedecin;
    private Long idAdmin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
