package com.chufesgesr.repositories;

import com.chufesgesr.entities.Notification;
import com.chufesgesr.entities.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByDestinataireId(Long destinataireId);
    
    List<Notification> findByDestinataireIdAndReadStatus(Long destinataireId, Boolean readStatus);
    
    List<Notification> findByType(NotificationType type);
    
    @Query("SELECT n FROM Notification n WHERE n.destinataire.id = :destinataireId ORDER BY n.dateCreation DESC")
    List<Notification> findByDestinataireIdOrderByDateCreationDesc(@Param("destinataireId") Long destinataireId);
}
