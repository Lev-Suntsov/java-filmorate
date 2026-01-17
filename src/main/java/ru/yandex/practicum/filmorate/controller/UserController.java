package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
public class UserController {
    private final HashMap<Integer, User> userMap = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(RuntimeException.class);

    @GetMapping("/users")
    public Collection<User> getUsers() {
        return userMap.values();
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        if (user.getBirthday() == null) {
            throw new RuntimeException("Поле дня рождения должно быть заполнено");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new RuntimeException("День рождения не может быть в будущем");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new RuntimeException("Логин должен быть указан");
        } else if (user.getLogin().contains(" ")) {
            throw new RuntimeException("извините, логин не может содержать пробелы");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RuntimeException("Укажите email пользователя");
        } else if (!user.getEmail().contains("@")) {
            throw new RuntimeException("Электронная почта указана не верно");
        }
        user.setId(getNextId());
        userMap.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        User oldUser;
        if (userMap.containsKey(user.getId())) {
            if (user.getBirthday() == null) {
                throw new RuntimeException("Поле дня рождения должно быть заполнено");
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new RuntimeException("День рождения не может быть в будущем");
            }

            if (user.getLogin() == null || user.getLogin().isBlank()) {
                throw new RuntimeException("Логин должен быть указан");
            } else if (user.getLogin().contains(" ")) {
                throw new RuntimeException("Логин не может содержать пробелы");
            }
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                throw new RuntimeException("Укажите email пользователя");
            } else if (!user.getEmail().contains("@")) {
                throw new RuntimeException("Электронная почта указана не верно");
            }
            oldUser = userMap.get(user.getId());
            oldUser.setName(user.getName());
            oldUser.setBirthday(user.getBirthday());
            oldUser.setEmail(user.getEmail());
            oldUser.setLogin(user.getLogin());
        } else {
            throw new RuntimeException("Пользователь не найден");
        }
        return oldUser;
    }

    private int getNextId() {
        int currentMaxId = userMap.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}