package com.example.employeebackend.service;

import com.example.employeebackend.entity.Department;
import com.example.employeebackend.repository.DepartmentRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentExcelService {

    private final DepartmentRepository departmentRepository;

    public DepartmentExcelService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // =========================
    // EXPORT
    // =========================
    public ByteArrayInputStream exportToExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Departments");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Dept ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Description");

            List<Department> list = departmentRepository.findAll();

            int rowIdx = 1;
            for (Department d : list) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(d.getDeptId());
                row.createCell(1).setCellValue(d.getName());
                row.createCell(2).setCellValue(d.getDescription());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Error exporting Excel: " + e.getMessage());
        }
    }

    // =========================
    // IMPORT
    // =========================
    public void importFromExcel(MultipartFile file) {
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());

            Sheet sheet = workbook.getSheetAt(0);
            List<Department> departments = new ArrayList<>();

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {

                Row row = sheet.getRow(i);
                if (row == null) continue;

                Department dept = new Department();

                dept.setDeptId((long) row.getCell(0).getNumericCellValue());
                dept.setName(row.getCell(1).getStringCellValue());
                dept.setDescription(row.getCell(2).getStringCellValue());

                departments.add(dept);
            }

            workbook.close();

            // Save to DB
            for (Department d : departments) {
                departmentRepository.save(d);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error importing Excel: " + e.getMessage());
        }
    }
}