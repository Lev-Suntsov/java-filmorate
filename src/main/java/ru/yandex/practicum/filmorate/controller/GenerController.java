package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.storage.Genre.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenerController {
    private static final Logger log = LoggerFactory.getLogger(GenerController.class);
    private final GenreService service;

    public GenerController(GenreService genreService) {
        this.service = genreService;
    }

    @GetMapping
    public List<GenreDto> getAll() {
        log.info("получаем список всех жанров");
        return service.getGenres();
    }

    @GetMapping("{id}")
    public GenreDto getById(@PathVariable int id) {
        log.info("получаем жанр по id");
        return service.getGenreById(id);
    }
}
