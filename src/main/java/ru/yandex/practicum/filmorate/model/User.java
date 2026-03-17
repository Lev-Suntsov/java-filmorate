package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

// User
@Data
@EqualsAndHashCode(of = "id")
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private String friendsStatus;
    private int friendsList;
    private int lickedFilms;
}

