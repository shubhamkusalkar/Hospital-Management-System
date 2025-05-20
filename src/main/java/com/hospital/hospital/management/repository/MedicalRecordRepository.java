package com.hospital.hospital.management.repository;

import com.hospital.hospital.management.model.Doctor;
import com.hospital.hospital.management.model.MedicalRecord;
import com.hospital.hospital.management.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatient(Patient patient);
    List<MedicalRecord> findByDoctor(Doctor doctor);
    List<MedicalRecord> findByRecordDateBetween(LocalDateTime start, LocalDateTime end);
    List<MedicalRecord> findByPatientOrderByRecordDateDesc(Patient patient);
} 