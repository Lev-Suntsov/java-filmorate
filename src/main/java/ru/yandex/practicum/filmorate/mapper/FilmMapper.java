package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.LinkedHashSet;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.xml.bind.ValidationException;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {

    public static Film mapToFilm(NewFilmRequest request) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());

        if (request.getMpa() != null) {
            Mpa mpa = new Mpa();
            mpa.setId(request.getMpa().getId());
            film.setMpa(mpa);
        }

        if (request.getGenres() != null) {
            Set<Genre> genres = request.getGenres().stream()
                    .map(g -> {
                        Genre genre = new Genre();
                        genre.setId(g.getId());
                        return genre;
                    })
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            film.setGenres(genres);
        } else {
            film.setGenres(new LinkedHashSet<>());
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
        if (request.hasDuration()) {
            film.setDuration(request.getDuration());
        }
        if (request.hasMpa()) {
            Mpa mpa = new Mpa();
            mpa.setId(request.getMpa().getId());
            film.setMpa(mpa);
        }
        if (request.hasGenres()) {
            Set<Genre> genres = request.getGenres().stream()
                    .map(g -> {
                        Genre genre = new Genre();
                        genre.setId(g.getId());
                        return genre;
                    })
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            film.setGenres(genres);
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
        dto.setMpa(film.getMpa());
        dto.setGenres(film.getGenres() == null
                ? new LinkedHashSet<>()
                : film.getGenres().stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        return dto;
    }
}