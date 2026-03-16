package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dto.NewUserReqest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.storage.user.UserStorage.UserDbStorage;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class FilmoRateApplicationTests {

    @Autowired
    private UserDbStorage userStorage;

    @Test
    void testFindUserById() throws ValidationException {
        NewUserReqest newUser = new NewUserReqest();
        newUser.setEmail("test@yandex.ru");
        newUser.setLogin("test_login");
        newUser.setName("Test Name");
        newUser.setBirthday(LocalDate.of(2000, 1, 1));
        newUser.setFriendsStatus("CONFIRMED");

        UserDto savedUser = userStorage.addUser(newUser);
        Long id = savedUser.getId();

        UserDto found = userStorage.getUserById(id);
        assertThat(found.getId()).isEqualTo(id);
    }
}

