package ru.yandex.practicum.filmorate;

import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.controller.UserController;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    User user;
    UserController controller;

    @BeforeEach
    void create() {
         controller = new UserController();
        user = new User();
        user.setName("");
    }

    @Test
    public void addUserTest() {
        user.setName("Лев");
        user.setEmail("leviksun.s@gmail.com");
        user.setLogin("leviksun.s@gmail.com");
        user.setBirthday(LocalDate.of(2003, 8, 8));
        User testUser = controller.addUser(user);
        user.setId(testUser.getId());
        assertEquals(true, controller.getUsers().contains(user));
    }

    @Test
    public void addUserIfNameNull() {
        user.setEmail("leviksun.s@gmail.com");
        user.setLogin("leviksun.s@gmail.com");
        user.setBirthday(LocalDate.of(2003, 8, 8));
        User testUser = controller.addUser(user);
        user.setId(testUser.getId());
        user.setName(user.getLogin());
        assertEquals(true, controller.getUsers().contains(user));
    }

    @Test
    public void addUserIfEmailNotCorrect() {
        user.setName("Лев");
        user.setEmail("leviksun.sgmail.com");
        user.setLogin("leviksun.s@gmail.com");
        user.setBirthday(LocalDate.of(2003, 8, 8));
        assertEquals(false, controller.getUsers().contains(user));
    }

    @Test
    public void addUserIfBirthdayNotCorrect() {
        user.setName("Лев");
        user.setEmail("leviksun.s@gmail.com");
        user.setLogin("leviksun.s@gmail.com");
        user.setBirthday(LocalDate.now().plusMonths(7));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.addUser(user));
        assertEquals(exception.getMessage(), "День рождения не может быть в будущем");
    }

    @Test
    public void addUserIfLoginNotCorrect() {
        user.setName("Лев");
        user.setEmail("leviksun.s@gmail.com");
        user.setBirthday(LocalDate.of(2003, 8, 8));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.addUser(user));
        assertEquals(exception.getMessage(), "Логин должен быть указан");

        user.setLogin("leviksun.s@gmail .com");
        exception = assertThrows(RuntimeException.class, () -> controller.addUser(user));
        assertEquals(exception.getMessage(), "извините, логин не может содержать пробелы");
    }

    @Test
    public void updateUserTest() {
        user.setName("Лев");
        user.setEmail("leviksun.s@gmail.com");
        user.setLogin("leviksun.s@gmail.com");
        user.setBirthday(LocalDate.of(2003, 8, 8));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.updateUser(user));
        assertEquals(exception.getMessage(), "Пользователь не найден");

        user.setId(controller.addUser(user).getId());
        user.setName("Lev3003947");
        controller.updateUser(user);
        assertEquals(true, controller.getUsers().contains(user));
    }
}
