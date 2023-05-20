package com.example.MySite.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity // Означает что это модель (models)
public class Games {
    @Id // Уникальный идентификатор
    @GeneratedValue(strategy = GenerationType.AUTO) // При добавлении новой записи случайно генерирует новый id
    private Long id;
    @NotBlank(message = "Пожалуйста, заполните поле!")
    @Length(max = 15, message = "Превышено допустимое количество символов!")
    private String name, developer;
    private boolean teamwork;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Games(){

    }

    public Games(String name, String developer, int teamwork, User user) {
        this.name = name;
        this.developer = developer;
        if (teamwork == 1) {
            this.teamwork = true;
        }
        else {
            this.teamwork = false;
        }
        this.author = user;
    }

    public String getAuthorName() {
        if (author != null) {
            return author.getUsername();
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

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public boolean isTeamwork() {
        return teamwork;
    }

    public void setTeamwork(boolean teamwork) {
        this.teamwork = teamwork;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
