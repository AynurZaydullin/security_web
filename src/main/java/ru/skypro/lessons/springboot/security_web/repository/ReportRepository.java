package ru.skypro.lessons.springboot.security_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.lessons.springboot.security_web.entities.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
}