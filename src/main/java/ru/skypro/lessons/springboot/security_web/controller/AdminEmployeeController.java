package ru.skypro.lessons.springboot.security_web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.lessons.springboot.security_web.entities.Employee;
import ru.skypro.lessons.springboot.security_web.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class AdminEmployeeController {

    private final EmployeeService employeeService;

    public AdminEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    // Post-запрос:
    //Добавление в базу данных списка новых сотрудников
    @PostMapping(value = "/upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(@RequestPart("employees") MultipartFile employees) {
        try {
            employeeService.addEmployees(employees);
        } catch (Throwable t) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    // Post-запрос:
    // Создание и загрузка в базу данных отчета о сотдрудниках
    @PostMapping(value = "/report/createReport")
    public long createReport() {
        try {
            return employeeService.createReport();
        } catch (Throwable t) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    // Put-запрос:
    // Обновление информации о сотруднике по переданному id
    @PutMapping("/update/{id}")
    public void updateEmployees(@PathVariable long id, @RequestBody  Employee employee) {
        try {
        employeeService.updateEmployee(id, employee);
        } catch (Throwable t) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    //Удаление сотрудника по переданному id
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable long id) {
        try {
            employeeService.deleteEmployee(id);
        }catch (Throwable t) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}