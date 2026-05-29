/*package com.example.employeebackend.repository;

import com.example.employeebackend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmpNo(String empNo);

    long countByActiveTrue();
    long countByActiveFalse();

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<Employee> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    long countByDepartment_NameIgnoreCase(String departmentName);

    // ✅ ADD THIS METHOD
    Optional<Employee> findByEmail(String email);
}*/
/*package com.example.employeebackend.repository;

import com.example.employeebackend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByIdAndDeletedFalse(Long id);

    Optional<Employee> findByIdAndActiveTrueAndDeletedFalse(Long id);

    List<Employee> findByDeletedFalse();

    List<Employee> findByActiveTrueAndDeletedFalse();

    List<Employee> findByActiveFalseAndDeletedFalse();

    boolean existsByEmail(String email);

    long countByActiveTrueAndDeletedFalse();

    long countByActiveFalseAndDeletedFalse();
    long countByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );
}*/

package com.example.employeebackend.repository;

import com.example.employeebackend.entity.Employee;
import com.example.employeebackend.entity.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);



    boolean existsByEmail(String email);

    long countByStatus(EmployeeStatus status);
    List<Employee> findByStatus(EmployeeStatus status);

    long countByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    // ✅ NEW: FOR EXPORT POPUP FILTERS (DOJ BASED)
    @Query("""
SELECT e FROM Employee e
WHERE (:status IS NULL OR e.status = :status)
AND (:fromDate IS NULL OR e.dateOfJoining >= :fromDate)
AND (:toDate IS NULL OR e.dateOfJoining <= :toDate)
""")
    List<Employee> findForExport(
            @Param("status") EmployeeStatus status,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );


    Optional<Employee> findByEmpNo(String empNo);



    List<Employee> findByManager(Employee manager);

    boolean existsByManager(Employee manager);
    @Query("""
SELECT COUNT(e) FROM Employee e
WHERE e.dateOfJoining <= CURRENT_DATE
AND (e.dateOfLeaving IS NULL OR e.dateOfLeaving > CURRENT_DATE)
""")
    long countCurrentlyActive();

    @Query("""
SELECT COUNT(e) FROM Employee e
WHERE e.dateOfLeaving IS NOT NULL
AND e.dateOfLeaving <= CURRENT_DATE
""")
    long countCurrentlyInactive();
    List<Employee> findByDateOfLeavingIsNull();
    List<Employee> findByDateOfLeavingIsNotNull();


}