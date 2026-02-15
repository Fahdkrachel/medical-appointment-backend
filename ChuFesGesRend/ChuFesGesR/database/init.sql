-- =====================================================
-- Script d'initialisation de la base de données
-- Système de gestion hospitalière ChuFesGesR
-- =====================================================

-- Création de la base de données
CREATE DATABASE IF NOT EXISTS hospital_management
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE hospital_management;

-- =====================================================
-- Table des utilisateurs (Patients, Médecins, Admins)
-- =====================================================
CREATE TABLE IF NOT EXISTS utilisateurs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom_complet VARCHAR(255) NOT NULL,
    telephone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(255),
    mot_de_passe VARCHAR(255) NOT NULL,
    role ENUM('PATIENT', 'MEDECIN', 'ADMIN'),
    id_medecin BIGINT,
    id_admin BIGINT,
    user_type VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_telephone (telephone),
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_user_type (user_type)
);

-- =====================================================
-- Table des spécialités
-- =====================================================
CREATE TABLE IF NOT EXISTS specialites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_nom (nom)
);

-- =====================================================
-- Table des services
-- =====================================================
CREATE TABLE IF NOT EXISTS services (
    id_service BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom_service VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    specialiste VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_nom_service (nom_service)
);

-- =====================================================
-- Table des plannings
-- =====================================================
CREATE TABLE IF NOT EXISTS plannings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    jour DATE NOT NULL,
    quota INT NOT NULL,
    compteur INT NOT NULL DEFAULT 0,
    medecin_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_medecin_jour (medecin_id, jour),
    INDEX idx_jour (jour)
);

-- =====================================================
-- Table des rendez-vous
-- =====================================================
CREATE TABLE IF NOT EXISTS appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status ENUM('EN_ATTENTE', 'ACCEPTE', 'REFUSE', 'ANNULE', 'REPORTE') NOT NULL DEFAULT 'EN_ATTENTE',
    scheduled_at DATETIME NOT NULL,
    note TEXT,
    patient_id BIGINT NOT NULL,
    medecin_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    planning_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_patient (patient_id),
    INDEX idx_medecin (medecin_id),
    INDEX idx_service (service_id),
    INDEX idx_planning (planning_id),
    INDEX idx_status (status),
    INDEX idx_scheduled_at (scheduled_at)
);

-- =====================================================
-- Table des pièces jointes
-- =====================================================
CREATE TABLE IF NOT EXISTS attachments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_path VARCHAR(500) NOT NULL,
    description TEXT,
    appointment_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_appointment (appointment_id)
);

-- =====================================================
-- Table des notifications
-- =====================================================
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('EMAIL', 'SMS', 'APP', 'GLOBAL') NOT NULL,
    message TEXT NOT NULL,
    read_status BOOLEAN NOT NULL DEFAULT FALSE,
    date_creation DATETIME NOT NULL,
    destinataire_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_destinataire (destinataire_id),
    INDEX idx_type (type),
    INDEX idx_read_status (read_status),
    INDEX idx_date_creation (date_creation)
);

-- =====================================================
-- Contraintes de clés étrangères
-- =====================================================

-- Contraintes pour la table plannings
ALTER TABLE plannings
ADD CONSTRAINT fk_planning_medecin
FOREIGN KEY (medecin_id) REFERENCES utilisateurs(id)
ON DELETE CASCADE;

-- Contraintes pour la table appointments
ALTER TABLE appointments
ADD CONSTRAINT fk_appointment_patient
FOREIGN KEY (patient_id) REFERENCES utilisateurs(id)
ON DELETE CASCADE,
ADD CONSTRAINT fk_appointment_medecin
FOREIGN KEY (medecin_id) REFERENCES utilisateurs(id)
ON DELETE CASCADE,
ADD CONSTRAINT fk_appointment_service
FOREIGN KEY (service_id) REFERENCES services(id_service)
ON DELETE CASCADE,
ADD CONSTRAINT fk_appointment_planning
FOREIGN KEY (planning_id) REFERENCES plannings(id)
ON DELETE CASCADE;

-- Contraintes pour la table attachments
ALTER TABLE attachments
ADD CONSTRAINT fk_attachment_appointment
FOREIGN KEY (appointment_id) REFERENCES appointments(id)
ON DELETE CASCADE;

-- Contraintes pour la table notifications
ALTER TABLE notifications
ADD CONSTRAINT fk_notification_destinataire
FOREIGN KEY (destinataire_id) REFERENCES utilisateurs(id)
ON DELETE CASCADE;

-- =====================================================
-- Données d'initialisation
-- =====================================================

-- Insertion des spécialités de base
INSERT INTO specialites (nom, description) VALUES
('Cardiologie', 'Spécialité médicale traitant les maladies du cœur et des vaisseaux sanguins'),
('Dermatologie', 'Spécialité médicale traitant les maladies de la peau'),
('Gastro-entérologie', 'Spécialité médicale traitant les maladies du tube digestif'),
('Neurologie', 'Spécialité médicale traitant les maladies du système nerveux'),
('Ophtalmologie', 'Spécialité médicale traitant les maladies des yeux'),
('Orthopédie', 'Spécialité chirurgicale traitant les maladies de l''appareil locomoteur'),
('Pédiatrie', 'Spécialité médicale traitant les maladies des enfants'),
('Psychiatrie', 'Spécialité médicale traitant les maladies mentales'),
('Radiologie', 'Spécialité médicale utilisant les rayons X et autres techniques d''imagerie'),
('Urologie', 'Spécialité chirurgicale traitant les maladies de l''appareil urinaire');

-- Insertion des services de base
INSERT INTO services (nom_service, description, specialiste) VALUES
('Service de Cardiologie', 'Service spécialisé dans le diagnostic et le traitement des maladies cardiovasculaires', 'Cardiologie'),
('Service de Dermatologie', 'Service spécialisé dans le diagnostic et le traitement des maladies de la peau', 'Dermatologie'),
('Service de Gastro-entérologie', 'Service spécialisé dans le diagnostic et le traitement des maladies digestives', 'Gastro-entérologie'),
('Service de Neurologie', 'Service spécialisé dans le diagnostic et le traitement des maladies neurologiques', 'Neurologie'),
('Service d''Ophtalmologie', 'Service spécialisé dans le diagnostic et le traitement des maladies oculaires', 'Ophtalmologie'),
('Service d''Orthopédie', 'Service spécialisé dans le diagnostic et le traitement des maladies de l''appareil locomoteur', 'Orthopédie'),
('Service de Pédiatrie', 'Service spécialisé dans le diagnostic et le traitement des maladies infantiles', 'Pédiatrie'),
('Service de Psychiatrie', 'Service spécialisé dans le diagnostic et le traitement des maladies mentales', 'Psychiatrie'),
('Service de Radiologie', 'Service spécialisé dans l''imagerie médicale et le diagnostic radiologique', 'Radiologie'),
('Service d''Urologie', 'Service spécialisé dans le diagnostic et le traitement des maladies urologiques', 'Urologie');

-- Insertion d'un administrateur par défaut
INSERT INTO utilisateurs (nom_complet, telephone, email, mot_de_passe, role, id_admin, user_type) VALUES
('Administrateur Principal', '0123456789', 'admin@hospital.com', 'admin123', 'ADMIN', 1, 'ADMIN');

-- Insertion de quelques médecins de test
INSERT INTO utilisateurs (nom_complet, telephone, email, mot_de_passe, role, id_medecin, user_type) VALUES
('Dr. Jean Dupont', '0123456790', 'jean.dupont@hospital.com', 'medecin123', 'MEDECIN', 1, 'MEDECIN'),
('Dr. Marie Martin', '0123456791', 'marie.martin@hospital.com', 'medecin123', 'MEDECIN', 2, 'MEDECIN'),
('Dr. Pierre Durand', '0123456792', 'pierre.durand@hospital.com', 'medecin123', 'MEDECIN', 3, 'MEDECIN');

-- Insertion de quelques patients de test
INSERT INTO utilisateurs (nom_complet, telephone, email, mot_de_passe, role, user_type) VALUES
('Patient Test 1', '0123456793', NULL, '123456', NULL, 'PATIENT'),
('Patient Test 2', '0123456794', NULL, '123456', NULL, 'PATIENT'),
('Patient Test 3', '0123456795', NULL, '123456', NULL, 'PATIENT');

-- =====================================================
-- Vues utiles pour les rapports
-- =====================================================

-- Vue des rendez-vous avec détails
CREATE OR REPLACE VIEW v_appointments_details AS
SELECT 
    a.id,
    a.status,
    a.scheduled_at,
    a.note,
    a.created_at,
    p.nom_complet as patient_nom,
    p.telephone as patient_telephone,
    m.nom_complet as medecin_nom,
    m.telephone as medecin_telephone,
    s.nom_service,
    pl.jour as planning_jour,
    pl.quota as planning_quota,
    pl.compteur as planning_compteur
FROM appointments a
JOIN utilisateurs p ON a.patient_id = p.id
JOIN utilisateurs m ON a.medecin_id = m.id
JOIN services s ON a.service_id = s.id_service
JOIN plannings pl ON a.planning_id = pl.id;

-- Vue des statistiques par service
CREATE OR REPLACE VIEW v_service_stats AS
SELECT 
    s.id_service,
    s.nom_service,
    COUNT(a.id) as total_appointments,
    COUNT(CASE WHEN a.status = 'ACCEPTE' THEN 1 END) as accepted_appointments,
    COUNT(CASE WHEN a.status = 'EN_ATTENTE' THEN 1 END) as pending_appointments,
    COUNT(CASE WHEN a.status = 'REFUSE' THEN 1 END) as refused_appointments,
    COUNT(CASE WHEN a.status = 'ANNULE' THEN 1 END) as cancelled_appointments
FROM services s
LEFT JOIN appointments a ON s.id_service = a.service_id
GROUP BY s.id_service, s.nom_service;

-- =====================================================
-- Procédures stockées utiles
-- =====================================================

DELIMITER //

-- Procédure pour créer un planning pour un médecin
CREATE PROCEDURE CreatePlanningForMedecin(
    IN p_medecin_id BIGINT,
    IN p_jour DATE,
    IN p_quota INT
)
BEGIN
    INSERT INTO plannings (medecin_id, jour, quota, compteur)
    VALUES (p_medecin_id, p_jour, p_quota, 0);
END //

-- Procédure pour vérifier la disponibilité d'un planning
CREATE PROCEDURE CheckPlanningAvailability(
    IN p_planning_id BIGINT,
    OUT p_available BOOLEAN,
    OUT p_remaining_slots INT
)
BEGIN
    DECLARE v_quota INT;
    DECLARE v_compteur INT;
    
    SELECT quota, compteur INTO v_quota, v_compteur
    FROM plannings
    WHERE id = p_planning_id;
    
    SET p_remaining_slots = v_quota - v_compteur;
    SET p_available = (v_compteur < v_quota);
END //

-- Procédure pour incrémenter le compteur d'un planning
CREATE PROCEDURE IncrementPlanningCounter(
    IN p_planning_id BIGINT
)
BEGIN
    UPDATE plannings 
    SET compteur = compteur + 1
    WHERE id = p_planning_id;
END //

DELIMITER ;

-- =====================================================
-- Index supplémentaires pour les performances
-- =====================================================

-- Index composites pour les requêtes fréquentes
CREATE INDEX idx_appointments_patient_status ON appointments(patient_id, status);
CREATE INDEX idx_appointments_medecin_status ON appointments(medecin_id, status);
CREATE INDEX idx_appointments_date_status ON appointments(scheduled_at, status);
CREATE INDEX idx_notifications_user_read ON notifications(destinataire_id, read_status);
CREATE INDEX idx_plannings_medecin_date ON plannings(medecin_id, jour);

-- =====================================================
-- Fin du script
-- =====================================================

SELECT 'Base de données hospital_management créée avec succès!' as message;
