package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.dto.NewUserReqest;
import ru.yandex.practicum.filmorate.dto.UpdateUserReqest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.storage.user.UserService.UserService;

import ru.yandex.practicum.filmorate.storage.user.UserStorage.UserDbStorage;

import javax.xml.bind.ValidationException;
import java.util.Collection;
import java.util.List;

@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    UserDbStorage userStorage;
    UserService service;

    public UserController(UserDbStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        service = userService;
    }

    @GetMapping("/users")
    public Collection<User> getUsers() {
        logger.info("получаем список пользователей");
        return userStorage.getUsers();
    }

    @PostMapping("/users")
    public UserDto addUser(@RequestBody NewUserReqest user) throws ValidationException {
        logger.info("добавляем информацию о пользователе");
        return userStorage.addUser(user);
    }

    @PutMapping("/users")
    public UserDto updateUser(@PathVariable long id, @RequestBody UpdateUserReqest user) throws ValidationException {
        logger.info("Обновляем информацию");
        return userStorage.updateUser(id, user);
    }

    @GetMapping("/users/{id}")
    public UserDto getUserById(@PathVariable int id) {
        logger.info("Получаем пользователя по id");
        return userStorage.getUserById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public List<UserDto> addFriend(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        logger.info("Добавляем пользователя в друзья");
        return service.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public List<UserDto> deleteUserFromFriendsList(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        logger.info("Удаляем пользователя из друзей");
        return  service.deleteUserFromFriendsList(userId, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<UserDto> getFriends(@PathVariable int id) {
        logger.info("Получаем список друзей пользователя {}", id);
        return service.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<UserDto> getTogetherFriends(@PathVariable("id") int id,@PathVariable("otherId") int otherId) {
        logger.info("Получаем список общих друзей");
        return service.getTogetherFriends(id, otherId);
    }
}