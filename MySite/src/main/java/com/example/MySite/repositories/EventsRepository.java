package com.example.MySite.repositories;

import com.example.MySite.models.Events;
import org.springframework.data.repository.CrudRepository;

public interface EventsRepository extends CrudRepository<Events, Long> {
}
