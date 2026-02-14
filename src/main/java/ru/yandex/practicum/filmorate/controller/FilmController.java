package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmService.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage.InMemoryFilmStorage;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class FilmController {
    private static final Logger logger = LoggerFactory.getLogger(FilmController.class);
    InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
    FilmService service = new FilmService();

    @GetMapping("/films")
    public Collection<Film> getFilms() {
        logger.info("Получаем список фильмов");
        return filmStorage.getFilms();
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        logger.info("Добавляем фильм");
        return filmStorage.addFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException  {
        logger.info("Обновляем информацию о фильме");
        return filmStorage.updateFilm(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable("id") int id) {
        logger.info("Получаем фильм по айди");
        return  filmStorage.getFilmById(id);
    }

    @GetMapping("/films/popula")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        logger.info("Получаем список популярных фильмов");
        return service.getPopularFilms(count);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public ArrayList<Film> lickedFilm(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        logger.info("Добавляем лайк фильму");
        return service.lickedFilm(userId, filmId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public ArrayList<Film> deleteFilmFromFavorit(@PathVariable("userId") int userId,@PathVariable("id") int filmId) {
        logger.info("Удаляем лайк");
        return service.deleteFilmFromFavorit(userId, filmId);
    }

}