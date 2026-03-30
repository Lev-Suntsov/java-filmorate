package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.storage.Genre.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private static final Logger log = LoggerFactory.getLogger(GenreController.class);
    private final GenreService service;

    public GenreController(GenreService genreService) {
        this.service = genreService;
    }

    @GetMapping
    public List<GenreDto> getAll() {
        log.info("Получаем список всех жанров");
        return service.findAll();
    }

    @GetMapping("/{id}")
    public GenreDto getById(@PathVariable int id) {
        log.info("Получаем жанр по id {}", id);
        return service.getGenreById(id);
    }
}