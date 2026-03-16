package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
    static final Logger logger = LoggerFactory.getLogger(FilmMapper.class);

    public static Film mapToFilm(NewFilmRequest request) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        // жанры собираешь по genreIds
        film.setMpa(Mpa.fromId(request.getMpaId())); // id -> enum/объект
        film.setDuration(request.getDuration());
        return film;
    }

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        // genreIds обратно
        dto.setMpaId(film.getMpa().getId());
        dto.setMpaName(film.getMpa().getName());
        dto.setDuration(film.getDuration());
        return dto;
    }

    public static Film updateFilm(Film film, UpdateFilmRequest request) throws ValidationException {
        logger.info("Проверяем, указанна ли продолжительность фильма");
        if (request.hasDuration()) {
            logger.info("обновляем продолжительность");
            film.setDuration(request.getDuration());
        } else {
            logger.warn("Ошибка. Укажите описание фильма");
        throw new ValidationException("продолжительность фильма должна быть указана");
    }

        logger.info("проверяем, указан ли жанр");
        if (request.hasGenre()) {
            logger.info("обновляем жанр");
            film.setGenre(Genre.fromId(request.getGenreIds()));
        } else {
            logger.warn("Ошибка, жанр должен быть указан");
            throw new ValidationException("Жанр фильма должен быть указан");
        }

        logger.info("Проверяем, указано ли имя");
        if (request.hasName()) {
            logger.info("Обновляем имя");
            film.setName(request.getName());
        } else {
            throw new ValidationException("название фильма должно быть указано");
        }

        if (request.hasMpa()) {
            film.setMpa(Mpa.fromId(request.getMpaId()));
        } else {
            throw new ValidationException("MPA фильма должен быть указан");
        }

        logger.info("Проверяем указанно ли описание");
        if (request.hasDescription()) {
            film.setDescription(request.getDescription());
        } else {
            throw new ValidationException("Описание должно быть указано");
        }

        if (film.getDescription().length() > 200) {
            logger.warn("Исключение, слишком большое описание");
            throw new ValidationException("описание не может быть больше 200 символов");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            logger.warn("Исключение, введена некорректная дата");
            throw new RuntimeException("Укажите корректную дату");
        }

        if (film.getDuration() == null || film.getDuration().isNegative() || film.getDuration().isZero()) {
            logger.warn("Исключение - некорректная продолжительность фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        if (request.hasReleaseDate()) {
            film.setReleaseDate(request.getReleaseDate());
        }
        return film;
    }
}
