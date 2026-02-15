package com.chufesgesr.dto;

import com.chufesgesr.entities.enums.AppointmentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Long id;
    private AppointmentStatus status;
    private LocalDateTime scheduledAt;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long patientId;
    private String patientNom;
    private Long medecinId;
    private String medecinNom;
    private Long serviceId;
    private String serviceNom;
    private Long planningId;
    private List<AttachmentDTO> attachments;
}
