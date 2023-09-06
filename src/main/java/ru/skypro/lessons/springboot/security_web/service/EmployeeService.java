package ru.skypro.lessons.springboot.security_web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.lessons.springboot.security_web.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.security_web.dto.ReportDTO;
import ru.skypro.lessons.springboot.security_web.exception.EmployeeNotFoundException;
import ru.skypro.lessons.springboot.security_web.exception.IllegalJsonFileException;
import ru.skypro.lessons.springboot.security_web.exception.InternalServerError;
import ru.skypro.lessons.springboot.security_web.exception.ReportNotFoundException;
import ru.skypro.lessons.springboot.security_web.repository.EmployeeRepository;
import ru.skypro.lessons.springboot.security_web.repository.ReportRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ReportRepository reportRepository;
    private final ObjectMapper objectMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ReportRepository reportRepository, ObjectMapper objectMapper) {
        this.employeeRepository = employeeRepository;
        this.reportRepository = reportRepository;
        this.objectMapper = objectMapper;
    }

    public void createBatchEmployees(List<EmployeeDTO> employees) {
        employees.stream()
                .map(EmployeeDTO::toEmployee)
                .forEach(employeeRepository::save);
    }
    public String buildReport() throws JsonProcessingException {
        List<ReportDTO> reports = employeeRepository.buildReports();
        return objectMapper.writeValueAsString(reports);
    }
    public void addEmployees(MultipartFile employees) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String extension = StringUtils.getFilenameExtension(employees.getOriginalFilename());
            if (!"json".equals(extension)) {
                throw new IllegalJsonFileException();
            }
            List<EmployeeDTO> employeeDTOS = objectMapper.readValue(
                    employees.getBytes(),
                    new TypeReference<>() {

                    }
            );
            createBatchEmployees(employeeDTOS);
        }catch (IOException e) {
            e.printStackTrace();
            throw new IllegalJsonFileException();
        }
    }

    public Long createReport() {
        try {
            Report report = new Report();
            report.setReport(buildReport());
            return reportRepository.save(report).getId();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalServerError();
        }
    }

    public Resource downloadReport(int id) {
        return new ByteArrayResource(
                reportRepository.findById(id)
                        .orElseThrow(ReportNotFoundException::new)
                        .getReport()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }
    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }
    public void deleteEmployee(long id) {
        employeeRepository.deleteById(id);
    }
    public void updateEmployee( long id, Employee newEmployee) {
        try {
            Employee oldEmployee = employeeRepository.findById(id)
                    .orElseThrow(EmployeeNotFoundException::new);
            oldEmployee.setName(newEmployee.getName());
            oldEmployee.setSalary(newEmployee.getSalary());
            employeeRepository.save(oldEmployee);
        } catch (EmployeeNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}