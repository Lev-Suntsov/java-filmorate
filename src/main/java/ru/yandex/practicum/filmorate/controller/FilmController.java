package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
public class FilmController {
    private final HashMap<Integer, Film> filmHashMap = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping("/films")
    public Collection<Film> getFilms() {
        return filmHashMap.values();
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new RuntimeException("Укажите имя фильма");
        }

        if (film.getDescription() == null || film.getDescription().isBlank()) {
            throw new RuntimeException("Укажите описание фильма");
        }

        if (film.getDescription().length() > 200) {
            throw new RuntimeException("описание не может быть больше 200 символов");
        }

        if (film.getReleaseDate() == null) {
            throw new RuntimeException("Укажите дату релиза");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new RuntimeException("Укажите корректную дату");
        }

        if (film.getDuration() == null) {
            throw new RuntimeException("Укажите продолжительность фильма");
        }

        if (film.getDuration() == null || film.getDuration().isNegative() || film.getDuration().isZero()) {
            throw new RuntimeException("Продолжительность фильма должна быть положительной");
        }

        film.setId(getNextId());
        filmHashMap.put(film.getId(), film);
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        if (!filmHashMap.containsKey(film.getId())) {
            throw new RuntimeException("фильм не найден");
        }

        Film oldFilm = filmHashMap.get(film.getId());
        // Валидация + обновление полей
        if (film.getName() == null || film.getName().isBlank()) {
            throw new RuntimeException("Укажите имя фильма");
        }

        if (film.getDescription() == null || film.getDescription().isBlank()) {
            throw new RuntimeException("Укажите описание фильма");
        }

        if (film.getDescription().length() > 200) {
            throw new RuntimeException("описание не может быть больше 200 символов");
        }

        if (film.getReleaseDate() == null) {
            throw new RuntimeException("Укажите дату релиза");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new RuntimeException("Укажите корректную дату");
        }

        if (film.getDuration() == null) {
            throw new RuntimeException("Укажите продолжительность фильма");
        }

        if (film.getDuration() == null || film.getDuration().isNegative() || film.getDuration().isZero()) {
            throw new RuntimeException("Продолжительность фильма должна быть положительной");
        }

        oldFilm.setName(film.getName());
        oldFilm.setDescription(film.getDescription());
        oldFilm.setReleaseDate(film.getReleaseDate());
        oldFilm.setDuration(film.getDuration());
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