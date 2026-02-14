package ru.yandex.practicum.filmorate.storage.user.UserStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

public class InMemoryUserStorage implements UserStorage {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryUserStorage.class);
    private final HashMap<Integer, User> userMap = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(RuntimeException.class);

    @Override
    public Collection<User> getUsers() {
        logger.info("Получаем информацию о пользователях");
        return userMap.values();
    }

    @Override
    public User addUser(User user) throws ValidationException {
        logger.info("Переходим в метод добавления нового пользователя");
        if (user.getBirthday() == null) {
            logger.warn("Исключение связанное с пустым полем дня рождения");
            throw new RuntimeException("Поле дня рождения должно быть заполнено");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            logger.warn("Возникло исключение связанное с некорректной датой рождения");
            throw new ValidationException("День рождения не может быть в будущем");
        }

        if (user.getLogin() == null || user.getLogin().isBlank()) {
            logger.warn("Возникло исключение - логин должен быть указан");
            throw new RuntimeException("Логин должен быть указан");
        } else if (user.getLogin().contains(" ")) {
            logger.warn("Логин не может содержать пробелы");
            throw new RuntimeException("извините, логин не может содержать пробелы");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            logger.warn("ЕСли имя неуказанно, именем становится логин");
            user.setName(user.getLogin());
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            logger.warn("Возникло исключение, email пользователя не указан");
            throw new RuntimeException("Укажите email пользователя");
        } else if (!user.getEmail().contains("@")) {
            logger.warn("Электронная почта указанна неверно");
            throw new ValidationException("Электронная почта указана не верно");
        }

        logger.info("Переходим к сохранению пользователя");
        user.setId(getNextId());
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        logger.info("Переходим в метод обновления ");
        User oldUser;
        if (userMap.containsKey(user.getId())) {
            if (user.getBirthday() == null) {
                logger.warn("Возникло исключение - не заполнено поле дня рождения");
                throw new RuntimeException("Поле дня рождения должно быть заполнено");
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                logger.warn("Ошибка валидации - некорректная дата дня рождения");
                throw new ValidationException("День рождения не может быть в будущем");
            }

            if (user.getLogin() == null || user.getLogin().isBlank()) {
                logger.warn("Возникло исключение - логин должен быть указан");
                throw new RuntimeException("Логин должен быть указан");
            } else if (user.getLogin().contains(" ")) {
                logger.warn("Указан некорректный логин");
                throw new ValidationException("Логин не может содержать пробелы");
            }

            if (user.getName() == null || user.getName().isBlank()) {
                logger.info("Если имя не указанно, приравниваем его к логину");
                user.setName(user.getLogin());
            }

            if (user.getEmail() == null || user.getEmail().isBlank()) {
                logger.info("Возникло исключение - email должен быть указан");
                throw new RuntimeException("Укажите email пользователя");
            } else if (!user.getEmail().contains("@")) {
                logger.info("Неверная электронная почта");
                throw new ValidationException("Электронная почта указана не верно");
            }

            logger.info("Обновляем информацию о пользователе");
            oldUser = userMap.get(user.getId());
            oldUser.setName(user.getName());
            oldUser.setBirthday(user.getBirthday());
            oldUser.setEmail(user.getEmail());
            oldUser.setLogin(user.getLogin());
        } else {
            logger.error("Данного пользователя не существует");
            throw new RuntimeException("Пользователь не найден");
        }

        return oldUser;
    }

    @Override
    public User getUserById(int id) {
        logger.info("Метод для поиска пользователя по id");
        return userMap.values().stream()
                .filter(user -> user.getId() == id)  // фильтруем по id
                .findFirst()                        // берём первый найденный
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден"));
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
