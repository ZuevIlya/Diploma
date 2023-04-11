package com.example.MySite.models;


import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Calendar;

@Entity
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name, organizer, description;
    private Calendar calendar;
    private int teams;
    private int count;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Games idGame;

    public Events() {
    }

    public Events(String name, String organizer, String description, Calendar calendar, int teams, int count, User user, Games game) {
        this.name = name;
        this.organizer = organizer;
        this.description = description;
        this.calendar = calendar;
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

    public Calendar getCalendar() {
        return calendar;
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
}
