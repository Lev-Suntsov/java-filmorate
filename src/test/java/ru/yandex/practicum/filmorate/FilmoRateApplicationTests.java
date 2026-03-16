package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test; // Ключевое исправление
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.storage.user.UserStorage.UserDbStorage;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class FilmoRateApplicationTests { // В JUnit 5 public не обязателен

    @Autowired
    private UserDbStorage userStorage;

    @Test
    void testFindUserById() {
        Optional<UserDto> userOptional = Optional.ofNullable(userStorage.getUserById(0L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 0L)
                );
    }
}
