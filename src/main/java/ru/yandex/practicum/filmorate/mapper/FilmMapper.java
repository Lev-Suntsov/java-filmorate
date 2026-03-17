package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.dto.*;
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
        film.setDuration(request.getDuration());


        if (request.getMpa() == null || request.getMpa().getId() <= 0) {
            throw new IllegalArgumentException("MPA с id=" +
                    (request.getMpa() == null ? 0 : request.getMpa().getId()) +
                    " не найден");
        }
        film.setMpa(Mpa.fromId(request.getMpa().getId()));

        if (request.getGenres() != null) {
            film.setGenres(
                    request.getGenres().stream()
                            .map(gd -> Genre.fromId(gd.getId()))
                            .toList()
            );
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

        if (request.hasGenre()) {
            film.setGenres(
                    request.getGenres().stream()
                            .map(gd -> Genre.fromId(gd.getId()))
                            .toList()
            );

            if (film.getDescription() != null && film.getDescription().length() > 200) {
                throw new ValidationException("описание не может быть больше 200 символов");
            }

            if (film.getReleaseDate() != null
                    && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("Укажите корректную дату релиза");
            }

            // updateFilm
            if (request.hasDuration()) {
                film.setDuration(request.getDuration());
            }
            if (film.getDuration() == null || film.getDuration() <= 0) {
                throw new ValidationException("Продолжительность фильма должна быть положительной");
            }
        }
        return film;
    }

        public static FilmDto mapToFilmDto (Film film){
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
    }