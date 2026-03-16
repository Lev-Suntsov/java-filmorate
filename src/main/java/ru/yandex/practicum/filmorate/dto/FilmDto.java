package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class FilmDto {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private java.util.List<GenreDto> genres;
    private MpaDto mpa;
    private Duration duration;
}


