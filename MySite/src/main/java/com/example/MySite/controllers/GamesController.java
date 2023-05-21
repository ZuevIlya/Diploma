package com.example.MySite.controllers;

import com.example.MySite.models.Games;
import com.example.MySite.models.User;
import com.example.MySite.repositories.GamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

// @PreAuthorize("hasAuthority('ADMIN')")
@Controller
public class GamesController {

    @Autowired // Создание переменной, которая ссылается на репозиторий
    private GamesRepository gamesRepository;

    @GetMapping("/games")
    public String games(Model model) {
        Iterable<Games> games = gamesRepository.findAll();
        model.addAttribute("gamesElements", games);
        return "games";
    }

    @GetMapping("/games/add")
    public String gamesAdd(Model model) {
        return "gamesAdd";
    }

    @PostMapping("/games/add")
    public String gamesPostAdd(@AuthenticationPrincipal User user,
                               @Valid Games game,
                               BindingResult bindingResult, // Список аргументов и сообщений ошибок валидации
                               Map<String, Object> model) {
        game.setAuthor(user);
        if (bindingResult.hasErrors()) { // Проверка на ошибки
            Map<String, String> errorsMap = UtilsController.getErrors(bindingResult);
            model.put("nameError", errorsMap.get("nameError:"));
            model.put("developerError", errorsMap.get("developerError:"));
            return "gamesAdd";
        } else {
            gamesRepository.save(game);
            return "redirect:/games"; // redirect - переадресация
        }
    }

    @GetMapping("/gameInfo/{id}")
    public String gamesDetails(@PathVariable(value = "id") long id, Model model) {
        if (!gamesRepository.existsById(id)) {
            return "redirect:/games"; // Если такой id не существует, то переходим на страницу games
        }
        Optional<Games> game = gamesRepository.findById(id);
        ArrayList<Games> result = new ArrayList<>();
        game.ifPresent(result::add);
        model.addAttribute("gameDetails", result);
        return "gamesDetails";
    }

    @GetMapping("/gameInfo/{id}/edit")
    public String gamesEdit(@PathVariable(value = "id") long id, Model model) {
        if (!gamesRepository.existsById(id)) {
            return "redirect:/games"; // Если такой id не существует, то переходим на страницу games
        }
        Optional<Games> game = gamesRepository.findById(id);
        ArrayList<Games> result = new ArrayList<>();
        game.ifPresent(result::add);
        model.addAttribute("gameDetails", result);
        return "gamesEdit";
    }

    @PostMapping("/gameInfo/{id}/edit")
    public String gamesPostEdit(@PathVariable(value = "id") long id,
                                @AuthenticationPrincipal User user,
                                @RequestParam String name,
                                @RequestParam String developer,
                                @RequestParam int teamwork,
                                @Valid Games game,
                                BindingResult bindingResult,
                                Map<String, Object> model) {

        if (bindingResult.hasErrors()) { // Проверка на ошибки
            Map<String, String> errorsMap = UtilsController.getErrors(bindingResult);
            model.put("nameError", errorsMap.get("nameError:"));
            model.put("developerError", errorsMap.get("developerError:"));
            model.put("id", id);
            model.put("gameDetails", game);
            return "gamesEdit";
        } else {
            game.setName(name);
            game.setDeveloper(developer);
            game.setAuthor(user);
            if (teamwork == 1){
                game.setTeamwork(true);
            } else {
                game.setTeamwork(false);
            }
            gamesRepository.save(game);
            return "redirect:/games"; // redirect - переадресация
        }
    }

    @PostMapping("/gameInfo/{id}/remove")
    public String gamesPostDelete(@PathVariable(value = "id") long id, Model model) {
        Games game = gamesRepository.findById(id).orElseThrow();
        gamesRepository.delete(game);
        return "redirect:/games"; // redirect - переадресация
    }

}
