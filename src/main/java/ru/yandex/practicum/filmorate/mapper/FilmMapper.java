package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.xml.bind.ValidationException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
    static final Logger logger = LoggerFactory.getLogger(FilmMapper.class);

    public static Film mapToFilm(NewFilmRequest request) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());

        int mpaId = request.getMpa() != null ? request.getMpa().getId() : 0;
        try {
            film.setMpa(Mpa.fromId(mpaId));
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("MPA с id=" + mpaId + " не найден");
        }

        if (request.getGenres() != null) {
            List<Genre> genres = request.getGenres().stream()
                    .map(gd -> {
                        int gid = gd.getId();
                        try {
                            return Genre.fromId(gid);
                        } catch (IllegalArgumentException e) {
                            throw new NotFoundException("Жанр с id=" + gid + " не найден");
                        }
                    })
                    .toList();

            film.setGenres(normalizeGenres(genres)); // ВАЖНО: присвоить результат
        }

        return film;
    }



    public static Film updateFilm(Film film, UpdateFilmRequest request) throws ValidationException {
        if (request.hasName()) {
            film.setName(request.getName());
        }

        if (request.hasDescription()) {
            film.setDescription(request.getDescription());
        }

        if (request.hasReleaseDate()) {
            film.setReleaseDate(request.getReleaseDate());
        }

        if (request.hasMpa()) {
            film.setMpa(Mpa.fromId(request.getMpa().getId()));
        }

        if (request.getGenres() != null) {
            List<Genre> genres = request.getGenres().stream()
                    .map(gd -> {
                        int gid = gd.getId();
                        try {
                            return Genre.fromId(gid);
                        } catch (IllegalArgumentException e) {
                            throw new NotFoundException("Жанр с id=" + gid + " не найден");
                        }
                    })
                    .toList();

            film.setGenres(normalizeGenres(genres)); // тоже присваиваем
        }

        // валидации как у тебя

        if (request.hasDuration()) {
            film.setDuration(request.getDuration());
        }
        if (film.getDuration() == null || film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        return film;
    }


    public static FilmDto mapToFilmDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());

        if (film.getMpa() != null) {
            MpaDto mpaDto = new MpaDto();
            mpaDto.setId(film.getMpa().getId());
            mpaDto.setName(film.getMpa().getName());
            dto.setMpa(mpaDto);
        }

        if (film.getGenres() != null) {
            dto.setGenres(
                    film.getGenres().stream()
                            .map(g -> {
                                GenreDto gd = new GenreDto();
                                gd.setId(g.getId());
                                gd.setName(g.getName());
                                return gd;
                            })
                            .toList()
            );
        }

        return dto;
    }

    private static List<Genre> normalizeGenres(List<Genre> genres) {
        if (genres == null) {
            return null;
        }
        return genres.stream()
                .collect(Collectors.toMap(Genre::getId, g -> g, (a, b) -> a)) // убираем дубли
                .values().stream()
                .sorted(Comparator.comparingInt(Genre::getId))               // сортируем по id
                .toList();
    }
    }