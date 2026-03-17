package ru.yandex.practicum.filmorate.storage.film.FilmService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage.UserDbStorage;

import java.util.*;

@Service
public class FilmService {
    private static final Logger logger = LoggerFactory.getLogger(FilmService.class);
    private final Map<Long, Set<Long>> filmLikes = new HashMap<>();
    UserDbStorage userStorage;
    FilmDbStorage filmStorage;

    public FilmService(FilmRepository repository, UserRepository userRepository) {
        this.filmStorage = new FilmDbStorage(repository);
        this.userStorage = new UserDbStorage(userRepository);
    }

    public ArrayList<FilmDto> lickedFilm(long userId, long filmId) {
        logger.info("Переходим в метод для добавления фильма в понравившиеся");
        userStorage.getUserById(userId);
        FilmDto film = filmStorage.getFilmById(filmId);

        filmLikes.computeIfAbsent(filmId, id -> new HashSet<>()).add(userId);

        return filmLikes.get(filmId).isEmpty()
                ? new ArrayList<>()
                : new ArrayList<>(List.of(film));
    }


    public ArrayList<FilmDto> deleteFilmFromFavorit(int userId, int filmId) {
        logger.info("Переходим в метод удаления фильма из понравившихся");
        userStorage.getUserById(userId);
        FilmDto film = filmStorage.getFilmById(filmId);

        if (!filmLikes.containsKey((long) filmId) ||
                !filmLikes.get((long) filmId).contains((long) userId)) {
            throw new NotFoundException("Фильма " + filmId + " нет в понравившихся у пользователя " + userId);
        }

        filmLikes.get((long) filmId).remove((long) userId);

        return new ArrayList<>(); // по ТЗ / тестам здесь обычно пустой список
    }


    public List<Film> getPopular(int count) {
        return filmStorage.getFilms().stream()
                .map(obj -> (Film) obj)
                .sorted(this::compareByLikesCount)   // уже готовый компаратор
                .limit(count)
                .toList();
    }

    private int compareByLikesCount(Film f1, Film f2) {
        long likes1 = filmLikes.getOrDefault(f1.getId(), Set.of()).size();
        long likes2 = filmLikes.getOrDefault(f2.getId(), Set.of()).size();
        return Long.compare(likes2, likes1); // по убыванию
    }
}

