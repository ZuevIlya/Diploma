package com.example.MySite.controllers;

import com.example.MySite.models.Events;
import com.example.MySite.models.Games;
import com.example.MySite.models.User;
import com.example.MySite.repositories.EventsRepository;
import com.example.MySite.repositories.GamesRepository;
import com.example.MySite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.*;

@Controller
public class EventsController {

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private GamesRepository gamesRepository;

    @Autowired
    private UserService userService;


    @GetMapping("/events/{idGame}")
    public String events(@PathVariable(value = "idGame") long idGame, Model model) {
        if (!gamesRepository.existsById(idGame)) {
            return "redirect:/games"; // Если такой id не существует, то переходим на страницу games
        }
        Iterable<Events> eventsList = eventsRepository.findAll(); // Изначально дастаём все события
        ArrayList<Events> result = new ArrayList<>();
        for (Events event: eventsList) {
            if (idGame == event.getIdGame().getId()) { // Если id текущей игры == id игры из события
                result.add(event);
            }
        }
        model.addAttribute("events", result);
        model.addAttribute("idGame", idGame);
        return "events";
    }

    @GetMapping("/events/{idGame}/add")
    public String eventsAdd(Model model) {
        return "eventsAdd";
    }

    @PostMapping("/events/{idGame}/add")
    public String eventsPostAdd(
                                @PathVariable(value = "idGame") long idGame,
                                @AuthenticationPrincipal User user,
                                @RequestParam String calendar,
                                @Valid Events event,
                                BindingResult bindingResult,
                                Map<String, Object> model) {

        Games game = gamesRepository.findById(idGame).orElseThrow();
        event.setIdGame(game);
        event.setAuthor(user);
        Calendar calendar2 = new GregorianCalendar();
        int year = Integer.parseInt(calendar.charAt(0) + "" + calendar.charAt(1) + "" + calendar.charAt(2) + "" + calendar.charAt(3));
        int month = Integer.parseInt(calendar.charAt(5) + "" + calendar.charAt(6));
        int day = Integer.parseInt(calendar.charAt(8) + "" + calendar.charAt(9));
        int hour = Integer.parseInt(calendar.charAt(11) + "" + calendar.charAt(12));
        int minute = Integer.parseInt(calendar.charAt(14) + "" + calendar.charAt(15));
        calendar2.set(year, month, day, hour, minute);
        event.setCalendar(calendar2);
        Map<String, String> errorsMap = UtilsController.getErrors(bindingResult);
        errorsMap.remove("calendarError:");
        if (!errorsMap.isEmpty()) { // Проверка на ошибки
            model.put("nameError", errorsMap.get("nameError:"));
            model.put("organizerError", errorsMap.get("organizerError:"));
            model.put("descriptionError", errorsMap.get("descriptionError:"));
            model.put("teamsError", errorsMap.get("teamsError:"));
            model.put("countError", errorsMap.get("countError:"));
            return "eventsAdd";
        } else {

            eventsRepository.save(event);
           // System.out.println(event.getCalendar());
            return "redirect:/events/" + idGame; // redirect - переадресация
        }
    }

    // попытка сделать список турниров
    @GetMapping("/user/events/{user_id}")
    public String eventsProfile(@PathVariable(value = "user_id") Long user_id, Model model) {
        Iterable<Events> eventsList = eventsRepository.findAll();
        ArrayList<Events> result = new ArrayList<>();

        for (Events event: eventsList) {
            if (user_id.equals(event.getAuthor().getId())) {
                result.add(event);
            }
        }

        model.addAttribute("events", eventsList);
        model.addAttribute("user", user_id);
        return "userEvents";

    }

    @PostMapping("/events/{event_id}/registration")
    public String eventRegistration(@PathVariable(value = "event_id") long event_id,
                                    @AuthenticationPrincipal User user,
                                    Model model) {

        Events event = eventsRepository.findById(event_id).orElseThrow();
        userService.registration(user, event);
        System.out.println(user.getName() + "  " + event.getName());
        return "redirect:/";
    }


}
