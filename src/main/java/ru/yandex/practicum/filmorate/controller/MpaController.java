package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.storage.Mpa.MpaService;

import java.util.List;

@RestController
public class MpaController {
    private static final Logger log = LoggerFactory.getLogger(MpaController.class);
    private final MpaService service;

    public MpaController(MpaService mpaService) {
        this.service = mpaService;
    }

    @GetMapping
    public List<MpaDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public MpaDto getById(@RequestParam("{id}") int id) {
        log.info("Получаем рейтинг MPA по id={}", id);
        return service.getById(id);
    }
}
