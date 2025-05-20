package com.hospital.hospital.management.repository;

import com.hospital.hospital.management.model.ERole;
import com.hospital.hospital.management.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
} 