package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmService.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage.FilmDbStorage;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class FilmController {
    private static final Logger logger = LoggerFactory.getLogger(FilmController.class);
     FilmDbStorage filmStorage;
    FilmService service;

    public FilmController(FilmDbStorage filmStorage, FilmService service) {
        this.filmStorage = filmStorage;
        this.service = service;
    }

    @GetMapping("/films")
    public Collection<Film> getFilms() {
        logger.info("Получаем список фильмов");
        return filmStorage.getFilms();
    }

    @PostMapping("/films")
    public FilmDto addFilm(@RequestBody NewFilmRequest film) throws ValidationException {
        logger.info("Добавляем фильм");
        return filmStorage.addFilm(film);
    }

    @PutMapping("/films")
    public FilmDto updateFilm(@RequestBody UpdateFilmRequest film, @RequestBody long id) throws ValidationException {
        logger.info("Обновляем информацию о фильме");
        return filmStorage.updateFilm(id, film);
    }

    @GetMapping("/films/{id}")
    public FilmDto getFilmById(@PathVariable("id") long id) {
        logger.info("Получаем фильм по айди");
        return filmStorage.getFilmById(id);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        logger.info("Получаем список популярных фильмов");
        return service.getPopularFilms(count);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public ArrayList<FilmDto> lickedFilm(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        logger.info("Добавляем лайк фильму");
        return service.lickedFilm(userId, filmId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public ArrayList<FilmDto> deleteFilmFromFavorit(@PathVariable("userId") int userId, @PathVariable("id") int filmId) {
        logger.info("Удаляем лайк");
        return service.deleteFilmFromFavorit(userId, filmId);
    }
}