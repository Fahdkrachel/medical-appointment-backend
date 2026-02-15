# 🏥 Système de Gestion Hospitalière ChuFesGesR

Un système complet de gestion hospitalière développé avec Spring Boot, Spring Data JPA et MySQL.

## 📋 Table des matières

- [Fonctionnalités](#-fonctionnalités)
- [Architecture](#-architecture)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [API Endpoints](#-api-endpoints)
- [Base de données](#-base-de-données)
- [Utilisation](#-utilisation)

## 🚀 Fonctionnalités

### 👥 Gestion des Utilisateurs
- **Patients** : Création de compte via SMS, gestion de profil, prise de rendez-vous
- **Médecins** : Gestion des plannings, consultation des rendez-vous, gestion des patients
- **Administrateurs** : Supervision complète, gestion des services, rapports statistiques

### 📅 Gestion des Rendez-vous
- Prise de rendez-vous par les patients
- Gestion des statuts (En attente, Accepté, Refusé, Annulé, Reporté)
- Système de planning avec quotas
- Notifications automatiques

### 🏥 Gestion des Services
- Création et gestion des services médicaux
- Association des médecins aux services
- Spécialités médicales

### 📱 Système de Notifications
- Notifications par email, SMS, application
- Notifications globales pour les administrateurs
- Suivi des notifications lues/non lues

### 📎 Gestion des Pièces Jointes
- Upload de fichiers médicaux
- Association aux rendez-vous
- Gestion des descriptions

## 🏗️ Architecture

```
src/
├── main/
│   ├── java/com/chufesgesr/
│   │   ├── entities/           # Entités JPA
│   │   ├── enums/             # Énumérations
│   │   ├── dto/               # Objets de transfert de données
│   │   ├── repositories/      # Interfaces JpaRepository
│   │   ├── services/          # Interfaces de services
│   │   │   └── impl/          # Implémentations des services
│   │   └── controllers/       # Contrôleurs REST
│   └── resources/
│       └── application.properties
└── database/
    └── init.sql              # Script d'initialisation MySQL
```

## 📋 Prérequis

- **Java 17** ou supérieur
- **Maven 3.6** ou supérieur
- **MySQL 8.0** ou supérieur
- **MySQL Workbench** (optionnel, pour la gestion de la base de données)

## 🔧 Installation

### 1. Cloner le projet
```bash
git clone <repository-url>
cd ChuFesGesR
```

### 2. Configurer la base de données MySQL

#### Option A : Utiliser le script SQL automatique
Le projet est configuré pour créer automatiquement la base de données au démarrage.

#### Option B : Exécuter le script manuellement
```bash
# Se connecter à MySQL
mysql -u root -p

# Exécuter le script d'initialisation
source database/init.sql
```

### 3. Configurer l'application

Modifiez le fichier `src/main/resources/application.properties` selon votre configuration MySQL :

```properties
# Configuration MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_management?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=votre_mot_de_passe
```

### 4. Compiler et exécuter
```bash
# Compiler le projet
mvn clean compile

# Exécuter l'application
mvn spring-boot:run
```

L'application sera accessible à l'adresse : `http://localhost:8080/api`

## ⚙️ Configuration

### Variables d'environnement importantes

| Variable | Description | Défaut |
|----------|-------------|---------|
| `spring.datasource.url` | URL de connexion MySQL | `jdbc:mysql://localhost:3306/hospital_management` |
| `spring.datasource.username` | Nom d'utilisateur MySQL | `root` |
| `spring.datasource.password` | Mot de passe MySQL | `password` |
| `server.port` | Port du serveur | `8080` |
| `jwt.secret` | Clé secrète JWT | `chufesgesrHospitalManagementSecretKey2024` |

## 🔌 API Endpoints

### 🔐 Authentification
```
POST /api/auth/register-patient     # Créer un compte patient
POST /api/auth/register-medecin     # Créer un compte médecin
POST /api/auth/register-admin       # Créer un compte admin
POST /api/auth/login                # Se connecter
POST /api/auth/confirm-patient      # Confirmer un compte patient
```

### 👥 Patients
```
GET    /api/patients/{id}                    # Récupérer un patient
GET    /api/patients/{id}/appointments       # Rendez-vous d'un patient
PUT    /api/patients/{id}/profile             # Mettre à jour le profil
PUT    /api/patients/{id}/password            # Changer le mot de passe
GET    /api/patients/{id}/notifications       # Notifications d'un patient
GET    /api/patients/{id}/appointments/history # Historique des rendez-vous
```

### 📅 Rendez-vous
```
POST   /api/appointments                      # Créer un rendez-vous
GET    /api/appointments/{id}                 # Récupérer un rendez-vous
PUT    /api/appointments/{id}/status          # Mettre à jour le statut
GET    /api/appointments/patient/{patientId}  # Rendez-vous par patient
GET    /api/appointments/medecin/{medecinId}  # Rendez-vous par médecin
GET    /api/appointments/service/{serviceId}   # Rendez-vous par service
GET    /api/appointments/status/{status}      # Rendez-vous par statut
DELETE /api/appointments/{id}                 # Supprimer un rendez-vous
GET    /api/appointments                      # Tous les rendez-vous
```

### 📋 Plannings
```
POST   /api/plannings                        # Créer un planning
GET    /api/plannings/{id}                   # Récupérer un planning
PUT    /api/plannings/{id}                   # Mettre à jour un planning
DELETE /api/plannings/{id}                   # Supprimer un planning
GET    /api/plannings/medecin/{medecinId}    # Plannings d'un médecin
GET    /api/plannings/medecin/{medecinId}/future # Plannings futurs d'un médecin
GET    /api/plannings/jour/{jour}            # Plannings par date
GET    /api/plannings/{id}/availability      # Vérifier disponibilité
GET    /api/plannings                        # Tous les plannings (admin)
```

### 🏥 Services
```
POST   /api/services                          # Créer un service
GET    /api/services/{id}                     # Récupérer un service
PUT    /api/services/{id}                     # Mettre à jour un service
DELETE /api/services/{id}                     # Supprimer un service
GET    /api/services                          # Tous les services
GET    /api/services/{id}/medecins            # Médecins d'un service
GET    /api/services/{id}/appointments        # Rendez-vous d'un service
```

### 📱 Notifications
```
POST   /api/notifications/global              # Notification globale
POST   /api/notifications/user/{userId}       # Notification utilisateur
GET    /api/notifications/user/{userId}       # Notifications d'un utilisateur
GET    /api/notifications/user/{userId}/unread # Notifications non lues
PUT    /api/notifications/{id}/read           # Marquer comme lue
GET    /api/notifications/{id}                # Récupérer une notification
DELETE /api/notifications/{id}                 # Supprimer une notification
GET    /api/notifications/type/{type}          # Notifications par type
```

## 🗄️ Base de données

### Structure des tables

#### Table `utilisateurs` (Single Table Inheritance)
- **Patients** : `role = NULL`, `email = NULL`, `mot_de_passe = code SMS`
- **Médecins** : `role = MEDECIN`, `email` requis, `mot_de_passe` classique
- **Admins** : `role = ADMIN`, `email` requis, `mot_de_passe` classique

#### Tables principales
- `utilisateurs` : Tous les utilisateurs (Patients, Médecins, Admins)
- `specialites` : Spécialités médicales
- `services` : Services hospitaliers
- `plannings` : Plannings des médecins
- `appointments` : Rendez-vous
- `attachments` : Pièces jointes
- `notifications` : Notifications

### Données d'initialisation

Le script `database/init.sql` inclut :
- 10 spécialités médicales de base
- 10 services hospitaliers
- 1 administrateur par défaut
- 3 médecins de test
- 3 patients de test

### Vues et procédures stockées

- `v_appointments_details` : Vue détaillée des rendez-vous
- `v_service_stats` : Statistiques par service
- `CreatePlanningForMedecin()` : Créer un planning
- `CheckPlanningAvailability()` : Vérifier la disponibilité
- `IncrementPlanningCounter()` : Incrémenter le compteur

## 💡 Utilisation

### 1. Démarrage rapide

```bash
# 1. Démarrer MySQL
# 2. Configurer application.properties
# 3. Lancer l'application
mvn spring-boot:run
```

### 2. Créer un compte patient

```bash
curl -X POST http://localhost:8080/api/auth/register-patient \
  -H "Content-Type: application/json" \
  -d '{
    "nomComplet": "Jean Dupont",
    "telephone": "0123456789"
  }'
```

### 3. Se connecter

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "telephone": "0123456789",
    "motDePasse": "123456"
  }'
```

### 4. Créer un rendez-vous

```bash
curl -X POST http://localhost:8080/api/appointments \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 1,
    "medecinId": 1,
    "serviceId": 1,
    "planningId": 1,
    "scheduledAt": "2024-01-15T10:00:00",
    "note": "Consultation de routine"
  }'
```

### 5. Envoyer une notification globale

```bash
curl -X POST http://localhost:8080/api/notifications/global \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Maintenance prévue ce soir à 22h",
    "cible": "TOUS"
  }'
```

## 🔍 Monitoring et Logs

### Logs de l'application
```bash
# Voir les logs en temps réel
tail -f logs/application.log
```

### Base de données
```sql
-- Vérifier les statistiques
SELECT * FROM v_service_stats;

-- Voir les rendez-vous détaillés
SELECT * FROM v_appointments_details;

-- Vérifier les utilisateurs
SELECT nom_complet, role, user_type FROM utilisateurs;
```

## 🛠️ Développement

### Structure du projet
```
ChuFesGesR/
├── src/main/java/com/chufesgesr/
│   ├── entities/           # Entités JPA (@Entity)
│   ├── enums/             # Énumérations
│   ├── dto/               # DTOs pour les réponses API
│   ├── repositories/      # Repositories JPA
│   ├── services/          # Services métier
│   └── controllers/       # Contrôleurs REST
├── src/main/resources/
│   └── application.properties
├── database/
│   └── init.sql
├── pom.xml
└── README.md
```

### Ajouter une nouvelle fonctionnalité

1. **Créer l'entité** dans `entities/`
2. **Créer le repository** dans `repositories/`
3. **Créer le service** dans `services/`
4. **Créer le contrôleur** dans `controllers/`
5. **Ajouter les tests** si nécessaire

### Tests

```bash
# Exécuter tous les tests
mvn test

# Exécuter les tests d'intégration
mvn test -Dtest=*IntegrationTest

# Exécuter les tests unitaires
mvn test -Dtest=*UnitTest
```

## 🚨 Dépannage

### Problèmes courants

#### 1. Erreur de connexion MySQL
```
Error: Communications link failure
```
**Solution** : Vérifier que MySQL est démarré et que les paramètres de connexion sont corrects.

#### 2. Erreur de port déjà utilisé
```
Error: Port 8080 is already in use
```
**Solution** : Changer le port dans `application.properties` ou arrêter l'application qui utilise le port.

#### 3. Erreur de base de données inexistante
```
Error: Unknown database 'hospital_management'
```
**Solution** : Exécuter le script `database/init.sql` ou vérifier que `createDatabaseIfNotExist=true` est dans l'URL.

### Logs utiles

```bash
# Voir les logs de démarrage
mvn spring-boot:run | grep -i "started"

# Voir les requêtes SQL
mvn spring-boot:run | grep -i "hibernate"

# Voir les erreurs
mvn spring-boot:run | grep -i "error"
```


---

**Développé avec fahd krachel  pour ChuFesGesR**
