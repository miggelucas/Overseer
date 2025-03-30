package com.overseer.repository;

import com.overseer.model.EventLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<EventLog, Long> {
    List<EventLog> findByApplication(String applicationName);
}