package com.example.MySite.controllers;

import com.example.MySite.models.Events;
import com.example.MySite.models.Games;
import com.example.MySite.repositories.EventsRepository;
import com.example.MySite.repositories.GamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class EventsController {

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private GamesRepository gamesRepository;


    @GetMapping("/events/{idGame}")
    public String events(@PathVariable(value = "idGame") Long idGame, Model model) {
        if (!gamesRepository.existsById(idGame)) {
            return "redirect:/games"; // Если такой id не существует, то переходим на страницу games
        }
        Iterable<Events> eventsList = eventsRepository.findAll(); // Изначально дастаём все события
        ArrayList<Events> result = new ArrayList<>();
        for (Events event: eventsList) {
            if (idGame.equals(event.getIdGame().getId())) { // Если id текущей игры == id игры из события
                result.add(event);
            }
        }
        model.addAttribute("events", result);
        return "events";
    }


}
