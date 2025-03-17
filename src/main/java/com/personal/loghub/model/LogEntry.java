package com.personal.loghub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public String service;
    public String level;
    public String message;
    public LocalDateTime timestamp;

    public LogEntry(String service, String level, String message) {
        this.service = service;
        this.level = level;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}
