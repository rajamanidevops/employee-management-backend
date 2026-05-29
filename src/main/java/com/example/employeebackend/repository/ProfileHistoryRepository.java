package com.example.employeebackend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.entity.ProfileHistory;

public interface ProfileHistoryRepository extends JpaRepository<ProfileHistory, Long> {
    List<ProfileHistory> findByProfile(Profile profile);
}