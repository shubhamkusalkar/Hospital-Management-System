package com.hospital.hospital.management.repository;

import com.hospital.hospital.management.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByLastNameContainingIgnoreCase(String lastName);
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByPhone(String phone);
} 