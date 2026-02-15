package com.chufesgesr.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDTO {
    private Long id;
    private String filePath;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
