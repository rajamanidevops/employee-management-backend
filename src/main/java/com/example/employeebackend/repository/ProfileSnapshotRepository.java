package com.example.employeebackend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.entity.ProfileSnapshot;

public interface ProfileSnapshotRepository extends JpaRepository<ProfileSnapshot, Long> {

    // Default find by profile
    Optional<ProfileSnapshot> findByProfile(Profile profile);

    // Fetch snapshot with profile eagerly
    @Query("SELECT s FROM ProfileSnapshot s JOIN FETCH s.profile p WHERE p.email = :email")
    Optional<ProfileSnapshot> findByEmailWithProfile(@Param("email") String email);
}