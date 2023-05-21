package com.example.MySite.models;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Entity
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Пожалуйста, заполните поле!")
    @Length(max = 30, message = "Превышено допустимое количество символов!")
    private String name, organizer;
    @NotBlank(message = "Пожалуйста, заполните поле!")
    @Length(max = 1024, message = "Превышено допустимое количество символов!")
    private String description;

    private Calendar calendar;

    @Range(min = 1, message = "Количество должно быть больше 0")
    private int teams;
    @Range(min = 1, message = "Количество должно быть больше 0")
    private int count;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Games idGame;

    @ManyToMany(mappedBy = "tournaments", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<User> eventsWithPlayers = new HashSet<>();


    public Events() {
    }

    public Events(String name, String organizer, String description, String calendar, int teams, int count, User user, Games game) {
        System.out.println(calendar);
        this.name = name;
        this.organizer = organizer;
        this.description = description;
        Calendar calendar2 = new GregorianCalendar();
        int year = Integer.parseInt(calendar.charAt(0) + "" + calendar.charAt(1) + "" + calendar.charAt(2) + "" + calendar.charAt(3));
        int month = Integer.parseInt(calendar.charAt(5) + "" + calendar.charAt(6));
        int day = Integer.parseInt(calendar.charAt(8) + "" + calendar.charAt(9));
        int hour = Integer.parseInt(calendar.charAt(11) + "" + calendar.charAt(12));
        int minute = Integer.parseInt(calendar.charAt(14) + "" + calendar.charAt(15));
        calendar2.set(year, month, day, hour, minute);
        this.calendar = calendar2;
        this.teams = teams;
        this.count = count;
        this.author = user;
        this.idGame = game;
    }

    public String getAuthorName() {
        if (author != null) {
            return author.getUsername();
        }
        else {
            return "<none>";
        }
    }

    public String getGameName() {
        if (idGame != null) {
            return idGame.getName();
        }
        else {
            return "<none>";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCalendar() {
        String string1; // day
        String string2; // month
        String string3; // year
        String string4; // hour
        String string5; // minute
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
            string1 = 0 + "" + calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            string1 = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        }

        if (calendar.get(Calendar.MONTH) < 10) {
            string2 = 0 + "" + calendar.get(Calendar.MONTH);
        } else {
            string2 = String.valueOf(calendar.get(Calendar.MONTH));
        }

        string3 = String.valueOf(calendar.get(Calendar.YEAR));

        if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
            string4 = 0 + "" + calendar.get(Calendar.HOUR_OF_DAY);
        } else {
            string4 = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        }

        if (calendar.get(Calendar.MINUTE) < 10) {
            string5 = 0 + "" + calendar.get(Calendar.MINUTE);
        } else {
            string5 = String.valueOf(calendar.get(Calendar.MINUTE));
        }

        String string = "Дата: " + string1 + "." + string2 + "." + string3 + "  Время: " + string4 + ":" + string5;
        return string;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getTeams() {
        return teams;
    }

    public void setTeams(int teams) {
        this.teams = teams;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Games getIdGame() {
        return idGame;
    }

    public void setIdGame(Games idGame) {
        this.idGame = idGame;
    }


    public Set<User> getEventsWithPlayers() {
        return eventsWithPlayers;
    }

    public void setEventsWithPlayers(Set<User> eventsWithPlayers) {
        this.eventsWithPlayers = eventsWithPlayers;
    }

}
