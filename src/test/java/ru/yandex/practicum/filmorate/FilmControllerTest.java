package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.Film;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    FilmController controller;
    Film film;

    @BeforeEach
    public void setController() {
        controller = new FilmController();
        film = new Film();
    }

    @Test
    public void addFilmTest() {
       RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.addFilm(film));
       assertEquals(exception.getMessage(), "Укажите имя фильма");

       film.setName("Форсаж");
       exception = assertThrows(RuntimeException.class, () -> controller.addFilm(film));
       assertEquals(exception.getMessage(), "Укажите описание фильма");

       film.setDescription("Фильм форсаж");
       exception = assertThrows(RuntimeException.class, () -> controller.addFilm(film));
       assertEquals(exception.getMessage(), "Укажите дату релиза");

       film.setReleaseDate(LocalDate.now());
       exception = assertThrows(RuntimeException.class, () -> controller.addFilm(film));
       assertEquals(exception.getMessage(), "Укажите продолжительность фильма");

       film.setDuration(Duration.ofHours(3));

       Film savedFilm = controller.addFilm(film);
       assertNotNull(savedFilm.getId());
       assertEquals(1, controller.getFilms().size());
       assertEquals("Форсаж", savedFilm.getName());
    }

}
