package ru.skypro.lessons.springboot.security_web.controller;


import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.lessons.springboot.security_web.service.Employee;
import ru.skypro.lessons.springboot.security_web.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employee")
    public List<Employee> getAllEmployees() {

        return employeeService.getAllEmployees();
    }
    @GetMapping("/report/{id}")
    public ResponseEntity<Resource> downloadReport(@PathVariable int id) {
        try {
            Resource resource = employeeService.downloadReport(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.json\"")
                    .body(resource);
        } catch (Throwable t) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}