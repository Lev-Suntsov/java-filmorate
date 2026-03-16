package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.NewUserReqest;
import ru.yandex.practicum.filmorate.dto.UpdateUserReqest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {
    public static User mapToUser(NewUserReqest reqest) {
        User user = new User();

        user.setLogin(reqest.getLogin());
        user.setEmail(reqest.getEmail());
        user.setBirthday(reqest.getBirthday());
        user.setName(reqest.getName());
        user.setFriendsStatus(reqest.getFriendsStatus());

        return user;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();

        dto.setBirthday(user.getBirthday());
        dto.setFriendsStatus(user.getFriendsStatus());
        dto.setName(user.getName());
        dto.setLogin(user.getLogin());
        dto.setEmail(user.getEmail());

        return dto;
    }

    public static User updateUser(User user, UpdateUserReqest request) {
        if (request.hasBirthday()) {
            user.setBirthday(request.getBirthday());
        }

        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }

        if ((request.hasLogin())) {
            user.setLogin(request.getLogin());
        }

        if (request.hasName()) {
            user.setName(request.getName());
        }

        if (request.hasFriendsStatus()) {
            user.setFriendsStatus(request.getFriendsStatus());
        }
        return user;
    }
}
