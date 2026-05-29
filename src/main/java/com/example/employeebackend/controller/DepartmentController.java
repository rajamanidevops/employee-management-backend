/*package com.example.employeebackend.controller;

import com.example.employeebackend.entity.Department;
import com.example.employeebackend.service.DepartmentService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "http://localhost:4200")
public class DepartmentController {

    private final DepartmentService departmentService;
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public List<Department> getAll() { return departmentService.getAll(); }

    @PostMapping
    public Department create(@RequestBody Department dept) { return departmentService.create(dept); }

    @PutMapping("/{id}")
    public Department update(@PathVariable Long id, @RequestBody Department dept) {
        return departmentService.update(id, dept);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { departmentService.delete(id); }
}*/

package com.example.employeebackend.controller;

import com.example.employeebackend.entity.Department;
import com.example.employeebackend.service.DepartmentExcelService;
import com.example.employeebackend.service.DepartmentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "http://localhost:4200")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentExcelService excelService;

    public DepartmentController(DepartmentService departmentService,
                                DepartmentExcelService excelService) {
        this.departmentService = departmentService;
        this.excelService = excelService;
    }

    // =========================
    // GET ALL
    // =========================
    @GetMapping
    public List<Department> getAll() {
        return departmentService.getAll();
    }

    // =========================
    // CREATE
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Department create(@RequestBody Department dept) {
        return departmentService.create(dept);
    }

    // =========================
    // UPDATE
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Department update(@PathVariable Long id,
                             @RequestBody Department dept) {
        return departmentService.update(id, dept);
    }

    // =========================
    // DELETE
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        departmentService.delete(id);
    }

    // =========================
    // EXPORT EXCEL
    // =========================
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportExcel() {

        ByteArrayInputStream in = excelService.exportToExcel();

        byte[] data = in.readAllBytes();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=departments.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }

    // =========================
    // IMPORT EXCEL
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/import")
    public ResponseEntity<String> importExcel(@RequestParam("file") MultipartFile file) {

        excelService.importFromExcel(file);

        return ResponseEntity.ok("Departments imported successfully");
    }
}



