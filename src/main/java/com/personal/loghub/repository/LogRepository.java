package com.personal.loghub.repository;

import com.personal.loghub.model.LogEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends CrudRepository<LogEntry, Long> {
}