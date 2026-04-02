package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Mpa.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private static final Logger log = LoggerFactory.getLogger(MpaController.class);
    private final MpaService service;

    public MpaController(MpaService mpaService) {
        this.service = mpaService;
    }

    @GetMapping
    public List<Mpa> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mpa> getById(@PathVariable int id) {
        log.info("Получаем рейтинг MPA по id={}", id);
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
