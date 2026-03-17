package ru.yandex.practicum.filmorate.storage.film.FilmService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage.UserDbStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class FilmService {
    private static final Logger logger = LoggerFactory.getLogger(FilmService.class);
    private final HashMap<UserDto, ArrayList<FilmDto>> favoritFilms = new HashMap<>();
    UserDbStorage userStorage;
    FilmDbStorage filmStorage;

    public FilmService(FilmRepository repository, UserRepository userRepository) {
        this.filmStorage = new FilmDbStorage(repository);
        this.userStorage = new UserDbStorage(userRepository);
    }

    public ArrayList<FilmDto> lickedFilm(long userId, long filmId) {
        logger.info("Переходим в метод для добавления фильма в понравившиеся");
        UserDto user = userStorage.getUserById(userId);
        FilmDto film =  filmStorage.getFilmById(filmId);

        if (!favoritFilms.containsKey(user)) {
            logger.debug("Проверяем, добавлен ли пользователь в таблицу");
            favoritFilms.put(user, new ArrayList<>());
        }

        logger.debug("Если существует, добавляем фильм");
        favoritFilms.get(user).add(film);

        logger.debug("Возвращаем лист с понравившимися фильмами");
        return new ArrayList<>(favoritFilms.get(user));
    }

    public ArrayList<FilmDto> deleteFilmFromFavorit(int userId, int filmId) {
        logger.info("Переходим в метод удаления фильма из понравившихся ");
        UserDto user = userStorage.getUserById(userId);
        FilmDto film = filmStorage.getFilmById(filmId);
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

    public List<Film> getPopular(int count) {
        return filmStorage.getFilms().stream()
                .map(obj -> (Film) obj)
                .sorted(this::compareByLikesCount)   // уже готовый компаратор
                .limit(count)
                .toList();
    }

    private int compareByLikesCount(Film f1, Film f2) {
        long likes1 = favoritFilms.values().stream().filter(set -> set.contains(f1.getId())).count();
        long likes2 = favoritFilms.values().stream().filter(set -> set.contains(f2.getId())).count();
        return Long.compare(likes2, likes1); // по убыванию
    }
}
