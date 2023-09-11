package ru.skypro.lessons.springboot.security_web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.lessons.springboot.security_web.service.Employee;
import ru.skypro.lessons.springboot.security_web.service.EmployeeService;

import java.util.List;

@RestController
public class InfoController {
    @Value("${app.env.dev}")
    private String dev;
    private final EmployeeService employeeService;

    public InfoController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/appInfo")
    public String getAppInfo() {
        return dev;
    }
}
