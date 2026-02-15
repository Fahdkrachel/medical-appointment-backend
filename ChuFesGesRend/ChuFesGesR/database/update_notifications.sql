-- Script pour mettre à jour la table notifications
-- Permettre les valeurs NULL pour destinataire_id (notifications globales)

-- Supprimer la contrainte NOT NULL existante
ALTER TABLE notifications MODIFY COLUMN destinataire_id BIGINT NULL;

-- Ajouter un index pour améliorer les performances
CREATE INDEX idx_notifications_destinataire_id ON notifications(destinataire_id);
CREATE INDEX idx_notifications_type ON notifications(type);
CREATE INDEX idx_notifications_read_status ON notifications(read_status);

-- Commentaire sur la table
ALTER TABLE notifications COMMENT = 'Table des notifications - destinataire_id peut être NULL pour les notifications globales';





