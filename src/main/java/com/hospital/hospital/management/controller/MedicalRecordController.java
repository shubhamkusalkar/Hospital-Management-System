package com.hospital.hospital.management.controller;

import com.hospital.hospital.management.model.Doctor;
import com.hospital.hospital.management.model.MedicalRecord;
import com.hospital.hospital.management.model.Patient;
import com.hospital.hospital.management.repository.DoctorRepository;
import com.hospital.hospital.management.repository.MedicalRecordRepository;
import com.hospital.hospital.management.repository.PatientRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();
        return new ResponseEntity<>(medicalRecords, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN') or hasRole('NURSE')")
    public ResponseEntity<MedicalRecord> getMedicalRecordById(@PathVariable("id") long id) {
        return medicalRecordRepository.findById(id)
                .map(medicalRecord -> new ResponseEntity<>(medicalRecord, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> createMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord,
                                                 @RequestParam Long patientId,
                                                 @RequestParam Long doctorId) {
        try {
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            
            medicalRecord.setPatient(patient);
            medicalRecord.setDoctor(doctor);
            medicalRecord.setRecordDate(LocalDateTime.now());
            
            MedicalRecord savedMedicalRecord = medicalRecordRepository.save(medicalRecord);
            return new ResponseEntity<>(savedMedicalRecord, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable("id") long id, 
                                                             @Valid @RequestBody MedicalRecord medicalRecordDetails) {
        return medicalRecordRepository.findById(id)
                .map(medicalRecord -> {
                    medicalRecord.setDiagnosis(medicalRecordDetails.getDiagnosis());
                    medicalRecord.setTreatment(medicalRecordDetails.getTreatment());
                    medicalRecord.setPrescription(medicalRecordDetails.getPrescription());
                    medicalRecord.setNotes(medicalRecordDetails.getNotes());
                    
                    MedicalRecord updatedMedicalRecord = medicalRecordRepository.save(medicalRecord);
                    return new ResponseEntity<>(updatedMedicalRecord, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteMedicalRecord(@PathVariable("id") long id) {
        try {
            medicalRecordRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN') or hasRole('NURSE')")
    public ResponseEntity<List<MedicalRecord>> getMedicalRecordsByPatient(@PathVariable Long patientId) {
        return patientRepository.findById(patientId)
                .map(patient -> {
                    List<MedicalRecord> medicalRecords = medicalRecordRepository.findByPatientOrderByRecordDateDesc(patient);
                    return new ResponseEntity<>(medicalRecords, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<MedicalRecord>> getMedicalRecordsByDoctor(@PathVariable Long doctorId) {
        return doctorRepository.findById(doctorId)
                .map(doctor -> {
                    List<MedicalRecord> medicalRecords = medicalRecordRepository.findByDoctor(doctor);
                    return new ResponseEntity<>(medicalRecords, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
} 