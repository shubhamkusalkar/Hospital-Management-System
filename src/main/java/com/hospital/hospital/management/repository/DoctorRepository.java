package com.hospital.hospital.management.repository;

import com.hospital.hospital.management.model.Doctor;
import com.hospital.hospital.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findBySpecialization(String specialization);
    Optional<Doctor> findByUser(User user);
    List<Doctor> findByLastNameContainingIgnoreCase(String lastName);
} 