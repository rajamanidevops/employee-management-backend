package com.example.employeebackend.service;

import com.example.employeebackend.entity.Department;
import com.example.employeebackend.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAll() { return departmentRepository.findAll(); }
    public Department getById(Long id) { return departmentRepository.findById(id).orElseThrow(); }
    public Department create(Department department) { return departmentRepository.save(department); }
    public Department update(Long id, Department dept) {
        Department department = departmentRepository.findById(id).orElseThrow();
        department.setName(dept.getName());
        department.setDescription(dept.getDescription());
        return departmentRepository.save(department);
    }
    public void delete(Long id) { departmentRepository.deleteById(id); }
}
