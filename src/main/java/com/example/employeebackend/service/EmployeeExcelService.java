package com.example.employeebackend.service;

import com.example.employeebackend.entity.Employee;
import com.example.employeebackend.entity.Department;
import com.example.employeebackend.entity.Role;
import com.example.employeebackend.repository.DepartmentRepository;
import com.example.employeebackend.repository.RoleRepository;
import com.example.employeebackend.repository.EmployeeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
@Service
public class EmployeeExcelService {

    private final EmployeeRepository employeeRepository;

    public EmployeeExcelService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    /* ===================== EXPORT ===================== */

    public ByteArrayInputStream exportToExcel(List<Employee> employees) {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Employees");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("EmpNo");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Email");
            header.createCell(3).setCellValue("Department");
            header.createCell(4).setCellValue("Role");
            header.createCell(5).setCellValue("Date Of Joining");
            header.createCell(6).setCellValue("Date Of Leaving");
            header.createCell(7).setCellValue("Manager");
            header.createCell(8).setCellValue("Status");

            int rowIndex = 1;

            for (Employee emp : employees) {

                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(emp.getEmpNo());
                row.createCell(1).setCellValue(emp.getName());
                row.createCell(2).setCellValue(emp.getEmail());

                row.createCell(3).setCellValue(
                        emp.getDepartment() != null
                                ? emp.getDepartment().getName()
                                : ""
                );

                row.createCell(4).setCellValue(
                        emp.getRole() != null
                                ? emp.getRole().getRole()
                                : ""
                );

                row.createCell(5).setCellValue(
                        emp.getDateOfJoining() != null
                                ? emp.getDateOfJoining().toString()
                                : ""
                );

                row.createCell(6).setCellValue(
                        emp.getDateOfLeaving() != null
                                ? emp.getDateOfLeaving().toString()
                                : ""
                );

                // ⚠️ Derived value (read-only)
                row.createCell(7).setCellValue(
                        emp.getManager() != null ? emp.getManager().getEmpNo() : ""
                );

                row.createCell(8).setCellValue(
                        emp.getStatus() != null ? emp.getStatus().name() : ""
                );
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Error while exporting Excel file", e);
        }
    }

    /* ===================== IMPORT ===================== */

    public List<Employee> importFromExcel(
            MultipartFile file,
            DepartmentRepository departmentRepository,
            RoleRepository roleRepository
    ) {

        List<Employee> employees = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null) continue;

                Employee emp = new Employee();

                emp.setEmpNo(getCellString(row.getCell(0)));
                emp.setName(getCellString(row.getCell(1)));
                emp.setEmail(getCellString(row.getCell(2)));

                // ===== DEPARTMENT =====
                String deptName = getCellString(row.getCell(3));
                Department dept = departmentRepository.findByName(deptName)
                        .orElseThrow(() ->
                                new RuntimeException("Department not found: " + deptName));
                emp.setDepartment(dept);

                // ===== ROLE =====
                String roleName = getCellString(row.getCell(4));
                Role role = roleRepository.findByRole(roleName)
                        .orElseThrow(() ->
                                new RuntimeException("Role not found: " + roleName));
                emp.setRole(role);

                // ===== MANAGER =====
                String managerEmpNo = getCellString(row.getCell(7));

                if (managerEmpNo != null && !managerEmpNo.trim().isEmpty()) {

                    Employee manager = employeeRepository.findByEmpNo(managerEmpNo)
                            .orElseThrow(() ->
                                    new RuntimeException("Manager not found: " + managerEmpNo));

                    emp.setManager(manager);

                } else {
                    emp.setManager(null);
                }

                // ===== DATE OF JOINING =====
                String doj = getCellString(row.getCell(5));
                emp.setDateOfJoining(parseDate(doj));

                // ===== DATE OF LEAVING =====
                String dol = getCellString(row.getCell(6));
                emp.setDateOfLeaving(parseDate(dol));

                // ❌ DO NOT IMPORT ACTIVE
                // Entity lifecycle manages it automatically

                employees.add(emp);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while importing Excel file", e);
        }

        return employees;
    }

    /* ===================== HELPER ===================== */


    private String getCellString(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();

            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue()
                            .toLocalDate()
                            .toString();
                } else {
                    yield String.valueOf((long) cell.getNumericCellValue());
                }
            }

            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());

            default -> "";
        };
    }
    private LocalDate parseDate(String value) {
        if (value == null || value.trim().isEmpty()) return null;

        try {
            return LocalDate.parse(value); // yyyy-MM-dd
        } catch (Exception e) {
            try {
                return LocalDate.parse(value,
                        java.time.format.DateTimeFormatter.ofPattern("M/d/yyyy")); // 5/24/2025
            } catch (Exception ex) {
                throw new RuntimeException("Invalid date format: " + value);
            }
        }
    }
}