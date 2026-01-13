package ru.yandex.practicum.filmorate.controller;

import model.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
public class FilmController {
    private  final HashMap<Integer, Film> filmHashMap = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(RuntimeException.class);

    @GetMapping
    public Collection<Film> getFilms() {
        return filmHashMap.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            throw new RuntimeException("Укажите имя фильма");
        }

        if (film.getDescription() == null || film.getDescription().isBlank()) {
            throw  new RuntimeException("Укажите описание фильма");

        } else if (film.getDescription().length() > 200) {
            throw new RuntimeException("описание не может быть больше 200 символов");
        }

        if (film.getReleaseDate() == null) {
            throw  new RuntimeException("Укажите дату релиза");

        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new RuntimeException("Укажите корректную дату");
        }

        if (film.getDuration() == null) {
            throw new RuntimeException("Укажите продолжительность фильма");
        }

        film.setId(getNextId());
        filmHashMap.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        Film oldFilm = null;
        if (filmHashMap.containsKey(film.getId())) {
            if (film.getName() == null || film.getName().isBlank()) {
                throw new RuntimeException("Укажите имя фильма");
            }

            if (film.getDescription() == null || film.getDescription().isBlank()) {
                throw  new RuntimeException("Укажите описание фильма");

            } else if (film.getDescription().length() > 200) {
                throw new RuntimeException("описание не может быть больше 200 символов");
            }

            if (film.getReleaseDate() == null) {
                throw  new RuntimeException("Укажите дату релиза");

            } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new RuntimeException("Укажите корректную дату");
            }

            if (film.getDuration() == null) {
                throw new RuntimeException("Укажите продолжительность фильма");
            }
            oldFilm = filmHashMap.get(film.getId());
            oldFilm.setDescription(film.getDescription());
            oldFilm.setDuration(film.getDuration());
            oldFilm.setName(film.getName());
            oldFilm.setReleaseDate(film.getReleaseDate());
            filmHashMap.put(oldFilm.getId(), oldFilm);
        } else {
            throw  new RuntimeException("фильм не найден");
        }
        return oldFilm;
    }

    private int getNextId() {
        int currentMaxId = filmHashMap.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
