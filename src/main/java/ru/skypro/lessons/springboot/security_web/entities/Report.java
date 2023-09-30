package ru.skypro.lessons.springboot.security_web.entities;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.Instant;

@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Lob
    @Column(columnDefinition = "oid")
    private String report;

    @CreationTimestamp(source = SourceType.DB)
    @Column(updatable = false)
    private Instant createAt;

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }
}