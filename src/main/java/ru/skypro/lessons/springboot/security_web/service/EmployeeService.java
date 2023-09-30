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
import ru.skypro.lessons.springboot.security_web.entities.Employee;
import ru.skypro.lessons.springboot.security_web.entities.Report;
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
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService(EmployeeRepository employeeRepository, ReportRepository reportRepository, ObjectMapper objectMapper) {
        this.employeeRepository = employeeRepository;
        this.reportRepository = reportRepository;
        this.objectMapper = objectMapper;
    }

    public void createBatchEmployees(List<EmployeeDTO> employees) {
        logger.info("Was invoked method: \"createBatchEmployees (save list of employeeDTOs into data base).\"");
        employees.stream()
                .map(EmployeeDTO::toEmployee)
                .forEach(employeeRepository::save);
    }
    public String buildReport() throws JsonProcessingException {
        logger.info("Was invoked method: \"buildReport (build Report of employees in data base).\"");
        List<ReportDTO> reports = employeeRepository.buildReports();
        return objectMapper.writeValueAsString(reports);
    }
    public void addEmployees(MultipartFile employees) {
        logger.info("Was invoked method: \"addEmployees (list of employees in data base).\"");
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
            logger.info("Was invoked method: \"downloadReport (Download Report of Employees).\"");
            Report report = new Report();
            report.setReport(buildReport());
            logger.debug("The nest invoked method is: \"save (reportRepository.save(report).getId()).\"");
            return reportRepository.save(report).getId();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalServerError();
        }
    }

    public Resource downloadReport(int id) {
        logger.info("Was invoked method: \"downloadReport (Download Report of Employees).\"");
        return new ByteArrayResource(
                reportRepository.findById(id)
                        .orElseThrow(ReportNotFoundException::new)
                        .getReport()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }
    public List<Employee> getAllEmployees() {
        logger.info("Was invoked method: \"getAllEmployees (get list of employees).\"");
        return employeeRepository.getAllEmployees();
    }
    public void deleteEmployee(long id) {
        logger.info("Was invoked method: \"deleteEmployee.\"");
        employeeRepository.deleteById(id);
    }
    public void updateEmployee( long id, Employee newEmployee) {
        try {
            logger.info("Was invoked method: \"updateEmployee.\"");
            Employee oldEmployee = employeeRepository.findById(id)
                    .orElseThrow(EmployeeNotFoundException::new);
            oldEmployee.setName(newEmployee.getName());
            oldEmployee.setSalary(newEmployee.getSalary());
            employeeRepository.save(oldEmployee);
        } catch (EmployeeNotFoundException e) {
            logger.error("There is no employee with id = " + id, e);
            throw new RuntimeException(e);
        }
    }
}