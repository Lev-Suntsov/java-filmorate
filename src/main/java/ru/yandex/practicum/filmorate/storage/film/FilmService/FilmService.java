package ru.yandex.practicum.filmorate.storage.film.FilmService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.LikeId;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage.UserDbStorage;

import java.util.*;

@Service
public class FilmService {
    private static final Logger logger = LoggerFactory.getLogger(FilmService.class);
    private final LikeRepository likeRepository;
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final JdbcTemplate jdbc;

    public FilmService(FilmRepository repository, UserRepository userRepository, FilmGenreRepository filmGenreRepository,
                       LikeRepository likeRepository, GenreRepository genreRepository, JdbcTemplate jdbc) {
        this.filmStorage = new FilmDbStorage(repository, filmGenreRepository, genreRepository);
        this.userStorage = new UserDbStorage(userRepository);
        this.likeRepository = likeRepository;
        this.jdbc = jdbc;
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

        String sql = """
        SELECT f.id, f.name, f.description, f.releaseDate, f.duration,
               f.mpa AS mpa_id, m.name AS mpa_name,
               COUNT(l.user_id) AS likes_count
        FROM films f
        LEFT JOIN mpa m ON f.mpa = m.id
        LEFT JOIN likes l ON f.id = l.film_id
        GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa, m.name
        ORDER BY likes_count DESC NULLS LAST
        LIMIT ?
        """;

        List<FilmDto> films = jdbc.query(sql, (rs, rowNum) -> {
            FilmDto film = new FilmDto();
            film.setId(rs.getInt("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
            film.setDuration(rs.getInt("duration"));

            int mpaId = rs.getInt("mpa_id");
            if (!rs.wasNull()) {
                Mpa mpa = new Mpa();
                mpa.setId(mpaId);
                mpa.setName(rs.getString("mpa_name"));
                film.setMpa(mpa);
            }

            film.setGenres(new LinkedHashSet<>());
            return film;
        }, count);

        return films;
    }

        public List<Long> getUserLikedFilms(long userId) {
            return likeRepository.findFilmIdsByUserId(userId);
        }
}

