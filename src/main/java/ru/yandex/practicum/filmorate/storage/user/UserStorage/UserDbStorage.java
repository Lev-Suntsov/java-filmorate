package ru.yandex.practicum.filmorate.storage.user.UserStorage;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.NewUserReqest;
import ru.yandex.practicum.filmorate.dto.UpdateUserReqest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.Collection;

@Repository
public class UserDbStorage implements UserStorage {
    private static final Logger logger = LoggerFactory.getLogger(UserDbStorage.class);
    UserRepository repository;

    public UserDbStorage(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDto addUser(NewUserReqest request) throws ValidationException {
        logger.info("Переходим в метод добавления нового пользователя");
        if (request.getBirthday() == null) {
            logger.warn("Исключение связанное с пустым полем дня рождения");
            throw new RuntimeException("Поле дня рождения должно быть заполнено");
        } else if (request.getBirthday().isAfter(LocalDate.now())) {
            logger.warn("Возникло исключение связанное с некорректной датой рождения");
            throw new ValidationException("День рождения не может быть в будущем");
        }

        if (request.getLogin() == null || request.getLogin().isBlank()) {
            logger.warn("Возникло исключение - логин должен быть указан");
            throw new RuntimeException("Логин должен быть указан");
        } else if (request.getLogin().contains(" ")) {
            logger.warn("Логин не может содержать пробелы");
            throw new RuntimeException("извините, логин не может содержать пробелы");
        }

        if (request.getName() == null || request.getName().isBlank()) {
            logger.warn("ЕСли имя неуказанно, именем становится логин");
            request.setName(request.getLogin());
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            logger.warn("Возникло исключение, email пользователя не указан");
            throw new RuntimeException("Укажите email пользователя");
        } else if (!request.getEmail().contains("@")) {
            logger.warn("Электронная почта указанна неверно");
            throw new ValidationException("Электронная почта указана не верно");
        }

        logger.info("Переходим к сохранению пользователя");
        User user = UserMapper.mapToUser(request);
        user = repository.saveUser(user);
        user.setId(user.getId());
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public Collection<User> getUsers() {
        logger.info("Ищем всех пользователей");
        return repository.findAllUsers();
    }

    @Override
    public UserDto updateUser(long id, UpdateUserReqest request) throws ValidationException {
        logger.info("Переходим в метод обновления ");
        User user = repository.getUserById(id).map(user1 ->  UserMapper.updateUser(user1, request)).orElseThrow(() -> new NotFoundException("Пользователь с id + " + id + " нен найден"));
        if (user.getBirthday() == null) {
            logger.warn("Возникло исключение - не заполнено поле дня рождения");
            throw new RuntimeException("Поле дня рождения должно быть заполнено");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            logger.warn("Ошибка валидации - некорректная дата дня рождения");
            throw new ValidationException("День рождения не может быть в будущем");
        }

        if (user.getLogin() == null || request.getLogin().isBlank()) {
            logger.warn("Возникло исключение - логин должен быть указан");
            throw new RuntimeException("Логин должен быть указан");
        } else if (user.getLogin().contains(" ")) {
            logger.warn("Указан некорректный логин");
            throw new ValidationException("Логин не может содержать пробелы");
        }

        if (user.getName() == null || request.getName().isBlank()) {
            logger.info("Если имя не указанно, приравниваем его к логину");
            request.setName(request.getLogin());
        }

        if (user.getEmail() == null || request.getEmail().isBlank()) {
            logger.info("Возникло исключение - email должен быть указан");
            throw new RuntimeException("Укажите email пользователя");
        } else if (!user.getEmail().contains("@")) {
            logger.info("Неверная электронная почта");
            throw new ValidationException("Электронная почта указана не верно");
        }
        user = repository.updateUser(user);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto getUserById(long id) {
        logger.info("Метод для поиска пользователя по id");
        return repository.getUserById(id).map(UserMapper::mapToUserDto).orElseThrow(() -> new NotFoundException("Пользователь с id + " + id + " нен найден"));
    }
}
