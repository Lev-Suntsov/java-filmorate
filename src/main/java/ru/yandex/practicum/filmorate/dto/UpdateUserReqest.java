package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserReqest {
    private Long id;
    private String email;
    private String login;
    private  String name;
    private LocalDate birthday;
    private  String friendsStatus;

    public boolean hasEmail() {
        return !(email == null || email.isBlank());
    }

    public boolean hasLogin() {
        return !(login == null || login.isBlank());
    }

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasBirthday() {
        return !(birthday == null);
    }

    public boolean hasFriendsStatus() {
        return !(friendsStatus == null || friendsStatus.isBlank());
    }
}
