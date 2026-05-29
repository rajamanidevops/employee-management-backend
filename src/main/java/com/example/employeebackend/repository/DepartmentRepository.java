package com.example.employeebackend.repository;

import com.example.employeebackend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // ✅ ADD THIS (IMPORTANT FOR EXCEL IMPORT)
    Optional<Department> findByName(String name);
    Optional<Department> findById(Long id);

}





