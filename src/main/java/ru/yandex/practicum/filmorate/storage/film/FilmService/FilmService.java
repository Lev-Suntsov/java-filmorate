package ru.yandex.practicum.filmorate.storage.film.FilmService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
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

    public FilmService(FilmRepository repository, UserRepository userRepository, FilmGenreRepository filmGenreRepository, LikeRepository likeRepository, GenreRepository genreRepository) {
        this.filmStorage = new FilmDbStorage(repository, filmGenreRepository, genreRepository);
        this.userStorage = new UserDbStorage(userRepository);
        this.likeRepository = likeRepository;
    }

    public void lickedFilm(long userId, long filmId) {
        logger.info("Переходим в метод для добавления фильма в понравившиеся");
        userStorage.getUserById(userId);
        filmStorage.getFilmById(filmId);

        if (likeRepository.exists(userId, filmId)) {
            logger.warn("Пользователь уже поставил фильм в понравившиеся");
            return;
        }

        Like like = new Like();
        like.setId(new LikeId(userId, filmId));
        likeRepository.addLike(userId, filmId);
    }


    public void unlikeFilm(Long userId, Long filmId) {
        logger.info("Удаление лайка: пользователь {} для фильма {}", userId, filmId);

        userStorage.getUserById(userId);

        if (!likeRepository.exists(userId, filmId)) {
            throw new NotFoundException(
                    String.format("Фильм %d не найден в лайках пользователя %d", filmId, userId)
            );

        }

        likeRepository.removeLike(userId, filmId);

        logger.info("Лайк успешно удалён: пользователь {}, фильм {}", userId, filmId);
    }


    public List<FilmDto> getPopularFilms(int count) {
        if (count <= 0) {
            return Collections.emptyList();
        }
        logger.info("Получаем топ-{} популярных фильмов", count);

        List<Film> films = new ArrayList<>(filmStorage.getFilms());

        if (films.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> filmIds = films.stream()
                .map(Film::getId)
                .collect(Collectors.toList());

        List<Object[]> likesData = likeRepository.countLikesByFilmIds(filmIds);

        Map<Long, Long> likesCount = likesData.stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(), // ID фильма
                        row -> ((Number) row[1]).longValue()  // Кол-во лайков
                ));



        return films.stream()
                .sorted((f1, f2) -> Long.compare(
                        likesCount.getOrDefault(f2.getId(), 0L),
                        likesCount.getOrDefault(f1.getId(), 0L)
                ))
                .limit(count)
                .map(film -> filmStorage.getFilmById(film.getId()))
                .collect(Collectors.toList());
    }

        public List<Long> getUserLikedFilms(long userId) {
            return likeRepository.findFilmIdsByUserId(userId);
        }
}

