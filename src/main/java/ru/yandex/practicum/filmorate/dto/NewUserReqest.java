package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewUserReqest {
    private String email;
    private String login;
    private  String name;
    private LocalDate birthday;
    private  String friendsStatus;
}
