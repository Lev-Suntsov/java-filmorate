package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@Table(name = "users")
public class User {
    @Id
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private String friendsStatus;
    private int friendsList;
    private int lickedFilms;
}

