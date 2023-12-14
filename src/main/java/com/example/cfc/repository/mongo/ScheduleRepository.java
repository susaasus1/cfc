package com.example.cfc.repository.mongo;

import com.example.cfc.model.mongo.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, Long> {
}
