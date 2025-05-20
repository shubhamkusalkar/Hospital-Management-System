# Hospital Management System

A comprehensive system to streamline the management of patient records for hospital staff. This application provides a secure, role-based platform for managing patients, doctors, appointments, and medical records in a hospital setting.

## 🏥 Overview

The Hospital Management System is designed to digitize and streamline hospital operations with a focus on:

- Secure authentication and authorization
- Patient record management
- Doctor profile management
- Appointment scheduling and tracking
- Medical records maintenance
- Role-based access restrictions

## 📋 Features

- **User Authentication & Authorization**
  - JWT-based secure authentication
  - Role-based access control
  - Password encryption
- **Role Management**
  - Admin: Full system access
  - Doctor: Patient, appointment and medical record management
  - Nurse: Limited patient and appointment access
  - User: Basic profile access
- **Patient Management**
  - Create/update patient profiles
  - Search patients by name
  - Store medical history and allergies
- **Doctor Management**
  - Manage doctor profiles and specializations
  - Link doctors to user accounts
  - Search by specialization or name
- **Appointment Scheduling**
  - Create, reschedule, and cancel appointments
  - Filter appointments by patient, doctor, or date
  - Status tracking (scheduled, completed, canceled)
- **Medical Records**
  - Create and maintain detailed medical records
  - Record diagnoses, treatments, and prescriptions
  - Secure access limited to authorized personnel

## 🏗️ System Architecture

```
                    ┌──────────────┐
                    │   Client     │
                    │  (Browser/   │
                    │ Mobile App)  │
                    └──────┬───────┘
                           │
                           ▼
┌─────────────────────────────────────────────┐
│               REST API Layer                 │
│  ┌─────────┐  ┌─────────┐  ┌──────────┐     │
│  │  Auth   │  │Resource │  │  Error   │     │
│  │Controller│ │Controllers│ │ Handling │     │
│  └─────────┘  └─────────┘  └──────────┘     │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│            Service Layer                     │
│  ┌─────────┐  ┌─────────┐  ┌──────────┐     │
│  │Business │  │ Security│  │   Data   │     │
│  │  Logic  │  │ Services│  │Validation│     │
│  └─────────┘  └─────────┘  └──────────┘     │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│            Repository Layer                  │
│  ┌─────────┐  ┌─────────┐  ┌──────────┐     │
│  │  Data   │  │  JPA    │  │   Query  │     │
│  │ Access  │  │Repositories│ Methods  │     │
│  └─────────┘  └─────────┘  └──────────┘     │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
             ┌───────────┐
             │  MySQL    │
             │ Database  │
             └───────────┘
```

The application follows a layered architecture:

1. **Presentation Layer (REST API)**: Controllers handling HTTP requests and responses
2. **Service Layer**: Business logic and security services
3. **Repository Layer**: Data access through JPA repositories
4. **Database Layer**: MySQL database for persistent storage

The system uses Spring Security with JWT for authentication and authorization, implementing stateless session management.

## 🔄 Application Flow

### Authentication Flow

```
┌─────────┐                   ┌───────────┐               ┌──────────┐              ┌──────────┐
│ Client  │                   │AuthController│             │UserService│             │ Database │
└────┬────┘                   └─────┬───────┘             └─────┬─────┘             └─────┬────┘
     │                              │                           │                         │
     │ POST /api/auth/signup        │                           │                         │
     │─────────────────────────────>│                           │                         │
     │                              │                           │                         │
     │                              │ createUser()              │                         │
     │                              │──────────────────────────>│                         │
     │                              │                           │ Save User               │
     │                              │                           │────────────────────────>│
     │                              │                           │                         │
     │                              │                           │ User Saved              │
     │                              │                           │<────────────────────────│
     │ Response                     │                           │                         │
     │<─────────────────────────────│                           │                         │
     │                              │                           │                         │
     │ POST /api/auth/signin        │                           │                         │
     │─────────────────────────────>│                           │                         │
     │                              │                           │                         │
     │                              │ authenticateUser()        │                         │
     │                              │──────────────────────────>│                         │
     │                              │                           │ Verify Credentials      │
     │                              │                           │────────────────────────>│
     │                              │                           │                         │
     │                              │                           │ User Details            │
     │                              │                           │<────────────────────────│
     │                              │                           │                         │
     │                              │ Generate JWT Token        │                         │
     │                              │<──────────────────────────│                         │
     │ JWT Token Response           │                           │                         │
     │<─────────────────────────────│                           │                         │
     │                              │                           │                         │
```

### Resource Access Flow

```
┌─────────┐               ┌─────────────┐           ┌──────────────┐        ┌───────────┐
│ Client  │               │JWT Auth Filter│          │Resource Controller│     │ Database  │
└────┬────┘               └───────┬─────┘           └──────┬─────────┘     └─────┬─────┘
     │                            │                         │                     │
     │ Request with JWT Token     │                         │                     │
     │───────────────────────────>│                         │                     │
     │                            │                         │                     │
     │                            │ Validate Token          │                     │
     │                            │─┐                       │                     │
     │                            │ │ Extract User Details  │                     │
     │                            │<┘                       │                     │
     │                            │                         │                     │
     │                            │ Set Authentication      │                     │
     │                            │─┐                       │                     │
     │                            │ │ Set Security Context  │                     │
     │                            │<┘                       │                     │
     │                            │                         │                     │
     │                            │ Forward Request         │                     │
     │                            │────────────────────────>│                     │
     │                            │                         │                     │
     │                            │                         │ Database Operations │
     │                            │                         │────────────────────>│
     │                            │                         │                     │
     │                            │                         │ Data                │
     │                            │                         │<────────────────────│
     │                            │                         │                     │
     │ Response                   │                         │                     │
     │<──────────────────────────────────────────────────────                     │
     │                            │                         │                     │
```

## 🗂️ Project Structure

```
src/main/java/com/hospital/hospital/management/
├── HospitalManagementApplication.java (Main application entry point)
├── config/                 (Application configurations)
├── controller/             (REST API controllers)
│   ├── AuthController.java        (Authentication endpoints)
│   ├── PatientController.java     (Patient management endpoints)
│   ├── DoctorController.java      (Doctor management endpoints)
│   ├── AppointmentController.java (Appointment management endpoints)
│   └── MedicalRecordController.java (Medical record endpoints)
├── exception/              (Custom exception handling)
├── model/                  (Domain entities)
│   ├── User.java                  (User account)
│   ├── Role.java                  (User roles)
│   ├── ERole.java                 (Role enum)
│   ├── Patient.java               (Patient information)
│   ├── Doctor.java                (Doctor information)
│   ├── Appointment.java           (Appointment details)
│   └── MedicalRecord.java         (Medical record)
├── payload/                (Request/Response objects)
│   ├── request/                   (Request DTOs)
│   │   ├── LoginRequest.java
│   │   └── SignupRequest.java
│   └── response/                  (Response DTOs)
│       ├── JwtResponse.java
│       └── MessageResponse.java
├── repository/             (Data access layer)
│   ├── UserRepository.java
│   ├── RoleRepository.java
│   ├── PatientRepository.java
│   ├── DoctorRepository.java
│   ├── AppointmentRepository.java
│   └── MedicalRecordRepository.java
└── security/               (Security configurations)
    ├── WebSecurityConfig.java     (Security settings)
    ├── jwt/                       (JWT implementation)
    │   ├── AuthEntryPointJwt.java
    │   ├── AuthTokenFilter.java
    │   └── JwtUtils.java
    └── services/                  (Security services)
        └── UserDetailsImpl.java
```

## 🔄 Data Flow

1. **User Registration & Authentication**:

   - User registration through `/api/auth/signup`
   - User authentication through `/api/auth/signin` to receive JWT token
   - JWT token used for subsequent API calls

2. **Patient and Doctor Management**:

   - Admin or doctors create patient records
   - Admin creates doctor profiles linked to user accounts
   - Patients and doctors can be searched and filtered

3. **Appointment Scheduling**:

   - Appointments created with doctor and patient references
   - Appointments can be rescheduled, canceled, or completed
   - Appointments can be filtered by doctor, patient, or date

4. **Medical Record Management**:
   - Doctors create medical records for patients
   - Records include diagnosis, treatment, and prescription
   - Records linked to both patient and doctor

## 💻 Technology Stack

- **Backend**: Java 17, Spring Boot 3.4.5
- **API**: RESTful APIs with Spring Web
- **Security**: Spring Security, JWT Authentication
- **Database Access**: Spring Data JPA, Hibernate ORM
- **Database**: MySQL 8.0
- **Containerization**: Docker, Docker Compose
- **Build Tool**: Maven

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Docker and Docker Compose

### Setup and Installation

#### Option 1: Using Docker (Recommended)

1. Clone the repository:

   ```
   git clone https://github.com/yourusername/hospital-management.git
   cd hospital-management
   ```

2. Build and run the application with Docker Compose:

   ```
   docker-compose up -d
   ```

   This will start both the MySQL database and the Spring Boot application.

3. The application will be available at: http://localhost:8080

#### Option 2: Manual Setup

1. Clone the repository:

   ```
   git clone https://github.com/yourusername/hospital-management.git
   cd hospital-management
   ```

2. Start MySQL:

   - If using Docker for MySQL only:
     ```
     docker-compose up -d mysql
     ```
   - Or use your local MySQL installation

3. Update application.properties:
   If running the application locally with MySQL in Docker, edit `src/main/resources/application.properties`:

   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
   ```

4. Build and run the application:

   ```
   mvn clean install
   mvn spring-boot:run
   ```

5. The application will be available at: http://localhost:8080

## 📝 API Documentation

### Authentication

- `POST /api/auth/signup` - Register a new user

  - Request body: username, email, password, roles (optional)
  - Response: Success message or error

- `POST /api/auth/signin` - Login and get JWT token
  - Request body: username, password
  - Response: JWT token, user ID, username, email, roles

### Patients

- `GET /api/patients` - Get all patients (DOCTOR, ADMIN, NURSE)
- `GET /api/patients/{id}` - Get a patient by ID (DOCTOR, ADMIN, NURSE)
- `POST /api/patients` - Create a new patient (DOCTOR, ADMIN)
  - Request body: patient details (firstName, lastName, etc.)
- `PUT /api/patients/{id}` - Update a patient (DOCTOR, ADMIN)
  - Request body: updated patient details
- `DELETE /api/patients/{id}` - Delete a patient (ADMIN only)
- `GET /api/patients/search?lastName=Smith` - Search patients by last name (DOCTOR, ADMIN, NURSE)

### Doctors

- `GET /api/doctors` - Get all doctors (public)
- `GET /api/doctors/{id}` - Get a doctor by ID (DOCTOR, ADMIN, NURSE)
- `POST /api/doctors?userId={userId}` - Create a new doctor (ADMIN only)
  - Request body: doctor details (firstName, lastName, specialization, etc.)
  - Query param: userId (user account to link to doctor profile)
- `PUT /api/doctors/{id}` - Update a doctor (DOCTOR, ADMIN)
  - Request body: updated doctor details
- `DELETE /api/doctors/{id}` - Delete a doctor (ADMIN only)
- `GET /api/doctors/specialization/{specialization}` - Get doctors by specialization (public)
- `GET /api/doctors/search?lastName=Smith` - Search doctors by last name (public)

### Appointments

- `GET /api/appointments` - Get all appointments (DOCTOR, ADMIN, NURSE)
- `GET /api/appointments/{id}` - Get an appointment by ID (DOCTOR, ADMIN, NURSE)
- `POST /api/appointments?patientId={patientId}&doctorId={doctorId}` - Create a new appointment (DOCTOR, ADMIN, NURSE)
  - Request body: appointment details (dateTime, reason, etc.)
  - Query params: patientId, doctorId
- `PUT /api/appointments/{id}` - Update an appointment (DOCTOR, ADMIN, NURSE)
  - Request body: updated appointment details
- `DELETE /api/appointments/{id}` - Delete an appointment (DOCTOR, ADMIN)
- `GET /api/appointments/patient/{patientId}` - Get appointments by patient (DOCTOR, ADMIN, NURSE)
- `GET /api/appointments/doctor/{doctorId}` - Get appointments by doctor (DOCTOR, ADMIN, NURSE)
- `GET /api/appointments/date?date=2023-06-01` - Get appointments by date (DOCTOR, ADMIN, NURSE)

### Medical Records

- `GET /api/medical-records` - Get all medical records (DOCTOR, ADMIN)
- `GET /api/medical-records/{id}` - Get a medical record by ID (DOCTOR, ADMIN, NURSE)
- `POST /api/medical-records?patientId={patientId}&doctorId={doctorId}` - Create a new medical record (DOCTOR only)
  - Request body: medical record details (diagnosis, treatment, etc.)
  - Query params: patientId, doctorId
- `PUT /api/medical-records/{id}` - Update a medical record (DOCTOR only)
  - Request body: updated medical record details
- `DELETE /api/medical-records/{id}` - Delete a medical record (ADMIN only)
- `GET /api/medical-records/patient/{patientId}` - Get medical records by patient (DOCTOR, ADMIN, NURSE)
- `GET /api/medical-records/doctor/{doctorId}` - Get medical records by doctor (DOCTOR, ADMIN)

## 🔒 Security

The application uses JWT (JSON Web Token) for authentication. Include the JWT token in the Authorization header for protected endpoints:

```
Authorization: Bearer <your_jwt_token>
```

### Security Implementation Details

1. **Authentication**:

   - Username/password authentication via AuthenticationManager
   - Successful authentication generates a JWT token
   - Token includes user ID, username, and roles

2. **Authorization**:

   - JWT Filter intercepts all requests
   - Validates token and extracts user details
   - Sets the SecurityContext with authentication information
   - Method-level security with @PreAuthorize annotations

3. **Password Security**:
   - Passwords stored using BCrypt hashing
   - Configurable password encoder strength

## 🔍 Role-Based Access Control

- **ADMIN**: Full access to all features

  - Create/update/delete all entities
  - Manage doctor profiles
  - View all system data

- **DOCTOR**: Access to patients, appointments, and medical records

  - Create/update patient records
  - Manage own appointments
  - Create and update medical records
  - View own medical records and patient history

- **NURSE**: Limited access to patient data and appointments

  - View patient details
  - Schedule and update appointments
  - View medical records (read-only)

- **USER**: Basic access for patients viewing their own data
  - View own profile information
  - View own appointments and medical records

## 🛠️ Troubleshooting

If you encounter connection issues when running with Docker:

1. Ensure both containers are running:

   ```
   docker ps
   ```

2. Check logs for any errors:

   ```
   docker logs hospital-mysql
   docker logs hospital-app
   ```

3. If you need to connect to the database directly:

   ```
   docker exec -it hospital-mysql mysql -uroot -ppassword
   ```

4. Common issues:
   - **Database connection refused**: Ensure MySQL container is running and exposing port 3306
   - **JWT validation failure**: Check token expiration or secret key configuration
   - **Role-based access issues**: Verify user has appropriate roles assigned

## 📊 Future Enhancements

- Frontend UI with React or Angular
- Email notifications for appointments
- Patient portal for self-service appointment booking
- Reporting and analytics dashboard
- Integration with external medical systems
- Mobile application for doctors and patients
