package ru.yandex.practicum.filmorate.storage.film.FilmService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class FilmService {
    private static final Logger logger = LoggerFactory.getLogger(FilmService.class);
    private final HashMap<User, ArrayList<Film>> favoritFilms = new HashMap<>();
    InMemoryUserStorage userStorage = new InMemoryUserStorage();
    InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();

    public ArrayList<Film> lickedFilm(int userId, int filmId) {
        logger.info("Переходим в метод для добавления фильма в понравившиеся");
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);

        if (!favoritFilms.containsKey(user)) {
            logger.debug("Проверяем, добавлен ли пользователь в таблицу");
            favoritFilms.put(user, new ArrayList<>());
        }

        logger.debug("Если существует, добавляем фильм");
        favoritFilms.get(user).add(film);

        logger.debug("Возвращаем лист с понравившимися фильмами");
        return new ArrayList<>(favoritFilms.get(user));
    }

    public ArrayList<Film> deleteFilmFromFavorit(int userId, int filmId) {
        logger.info("Переходим в метод удаления фильма из понравившихся ");
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);
        logger.info("Проверяем, есть ли пользователя лайки");

        if (!favoritFilms.containsKey(user)) {
            logger.warn("У пользователя нет понравившихся фильмов");
            throw new NotFoundException("У пользователя " + userId + " нет лайков ни одному фильму");
        }
        logger.info("Проверяем, есть фильм в списке понравившихся");

        if (!favoritFilms.get(user).contains(film)) {
            logger.debug("Возникло исключение. данного фильма нет в списке понравившихся");
            throw new NotFoundException("Фильма " + filmId + " нет в списке понравившихся");
        }
        logger.info("Удаляем фильм");
        favoritFilms.get(user).remove(film);

        return new ArrayList<>(favoritFilms.get(user));
    }

    public List<Film> getPopularFilms(int count) {
        logger.info("Переходим в метод получения 10 самых популярных фильмов");
        return filmStorage.getFilms().stream()
                .sorted(this::compareByLikesCount)  // сортировка по лайкам
                .limit(Math.max(count, 10))         // минимум 10
                .toList();
    }

    private int compareByLikesCount(Film f1, Film f2) {
        long likes1 = favoritFilms.values().stream().filter(set -> set.contains(f1.getId())).count();
        long likes2 = favoritFilms.values().stream().filter(set -> set.contains(f2.getId())).count();
        return Long.compare(likes2, likes1);  // убывание
    }
}
