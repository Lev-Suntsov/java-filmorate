package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
    static final Logger logger = LoggerFactory.getLogger(FilmMapper.class);

    public static Film mapToFilm(NewFilmRequest request) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());

        film.setMpa(Mpa.fromId(request.getMpaId()));

        if (request.hasGenre()) {
            film.setGenre(Genre.fromId(request.getGenreId()));
        }


        if (request.getGenreIds() != null) {
            film.setGenres(
                    request.getGenreIds().stream()
                            .map(Long::intValue)
                            .map(Genre::fromId)
                            .toList()
            );
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

        if (film.getGenre() != null) { // <-- тоже plural
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
}