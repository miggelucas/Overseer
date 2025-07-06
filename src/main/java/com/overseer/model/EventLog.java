package com.overseer.model;

import com.overseer.util.Level;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "event_log")
public class EventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String application;
    private Level level;
    private String message;

    private Instant createdAt;
    private Instant updatedAt;
}
