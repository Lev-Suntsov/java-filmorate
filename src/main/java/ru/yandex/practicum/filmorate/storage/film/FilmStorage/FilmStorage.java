package ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

import javax.xml.bind.ValidationException;
import java.util.Collection;

public interface FilmStorage {
    public Collection<Film> getFilms();

    public FilmDto addFilm(NewFilmRequest request) throws RuntimeException;

    public FilmDto updateFilm(long id, UpdateFilmRequest request) throws ValidationException;

    public FilmDto getFilmById(long id);
}
