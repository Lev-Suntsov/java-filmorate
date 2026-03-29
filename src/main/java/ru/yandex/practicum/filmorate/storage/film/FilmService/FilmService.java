package ru.yandex.practicum.filmorate.storage.film.FilmService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmGenreRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.LikeRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.LikeId;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage.UserDbStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private static final Logger logger = LoggerFactory.getLogger(FilmService.class);
    private final LikeRepository likeRepository;
    UserDbStorage userStorage;
    FilmDbStorage filmStorage;

    public FilmService(FilmRepository repository, UserRepository userRepository, FilmGenreRepository filmGenreRepository, LikeRepository likeRepository) {
        this.filmStorage = new FilmDbStorage(repository, filmGenreRepository);
        this.userStorage = new UserDbStorage(userRepository);
        this.likeRepository = likeRepository;
    }

    public void lickedFilm(long userId, long filmId) {
        logger.info("Переходим в метод для добавления фильма в понравившиеся");
        userStorage.getUserById(userId);
        filmStorage.getFilmById(filmId);

        if (likeRepository.existsByUserIdAndFilmId(userId, filmId)) {
            logger.warn("Пользователь уже поставил фильм в понравившиеся");
            return;
        }

        Like like = new Like();
        like.setId(new LikeId(userId, filmId));
        likeRepository.save(like);
    }


    public void unlikeFilm(Long userId, Long filmId) {
        logger.info("Удаление лайка: пользователь {} для фильма {}", userId, filmId);

        // Проверяем существование пользователя
        userStorage.getUserById(userId);

        // Проверяем наличие лайка
        if (!likeRepository.existsByUserIdAndFilmId(userId, filmId)) {
            throw new NotFoundException(
                    String.format("Фильм %d не найден в лайках пользователя %d", filmId, userId)
            );

        }

        // Удаляем лайк в рамках транзакции
        likeRepository.deleteById(filmId);

        logger.info("Лайк успешно удалён: пользователь {}, фильм {}", userId, filmId);
    }


    public List<FilmDto> getPopularFilms(int count) {
        logger.info("Получаем топ-{} популярных фильмов", count);
        return filmStorage.getFilms().stream()
                .map(film -> filmStorage.getFilmById(film.getId()))
                .sorted((f1, f2) -> Long.compare(
                        likeRepository.countLikesByFilmId(Long.getLong(f2.getId().toString())),
                        likeRepository.countLikesByFilmId(Long.getLong(f1.getId().toString()))
                ))
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Long> getUserLikedFilms(long userId) {
        return likeRepository.findFilmIdsByUserId(userId);
    }
}

