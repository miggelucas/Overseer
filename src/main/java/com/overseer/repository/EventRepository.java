package com.overseer.repository;

import com.overseer.model.EventLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<EventLog, Long> {
}