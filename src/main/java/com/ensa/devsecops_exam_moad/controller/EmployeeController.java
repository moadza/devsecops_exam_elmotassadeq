package com.ensa.devsecops_exam_moad.controller;

import com.ensa.devsecops_exam_moad.module.Employee;
import com.ensa.devsecops_exam_moad.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getEmployeeById(@PathVariable Long id, @RequestParam(required = false, defaultValue = "fr") String lang) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);

        if (employee.isPresent()) {
            return ResponseEntity.ok(employee.get().toString()); // Or however you want to represent the employee
        } else {
            String message = lang.equalsIgnoreCase("en") ?
                    "Employee not found" : "Employé introuvable";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    @PostMapping
    public ResponseEntity<String> createEmployee(@RequestBody Employee employee, @RequestParam(required = false, defaultValue = "fr") String lang) {
        boolean emailExists = employeeService.emailExists(employee.getEmail()); // Assume this checks for duplicate emails

        if (emailExists) {
            String message = lang.equalsIgnoreCase("en") ?
                    "Email already in use" : "Email déjà utilisé";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }

        employeeService.saveEmployee(employee);
        String message = lang.equalsIgnoreCase("en") ?
                "Employee added successfully" : "Employé ajouté avec succès";
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return updatedEmployee != null ? ResponseEntity.ok(updatedEmployee) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
