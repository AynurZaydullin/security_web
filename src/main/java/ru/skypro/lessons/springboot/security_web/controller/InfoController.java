package ru.skypro.lessons.springboot.security_web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
    @Value("${app.env}")
    private final String dev = null;


    @GetMapping("/appInfo")
    public String getAppInfo() {
        return dev;
    }
}