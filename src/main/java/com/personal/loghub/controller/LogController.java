package com.personal.loghub.controller;

import com.personal.loghub.model.LogEntry;
import com.personal.loghub.service.LogService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public LogEntry createLog(@RequestBody LogEntry logEntry) {
        return logService.save(logEntry);
    }

    @GetMapping
    public List<LogEntry> getPins() {
        return logService.getAllLogs();
    }
}
