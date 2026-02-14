package ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import ru.yandex.practicum.filmorate.model.Film;

import javax.xml.bind.ValidationException;
import java.util.Collection;

public interface FilmStorage {
    public Collection<Film> getFilms();

    public Film addFilm(Film film) throws ValidationException;

    public Film updateFilm(Film film) throws ValidationException;

    public Film getFilmById(int id);
}
