package com.example.MySite.repositories;

import com.example.MySite.models.Games;
import org.springframework.data.repository.CrudRepository;

public interface GamesRepository extends CrudRepository<Games, Long> {
    // CrudRepository имеет все необходимые функции для создания/удаления/редактирования таблиц
}
