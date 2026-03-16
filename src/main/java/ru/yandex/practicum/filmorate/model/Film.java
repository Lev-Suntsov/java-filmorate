package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
public class Film {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Genre genre;
    private Mpa mpa;
    private Integer genreId;
    private List<Genre> genres;

    @JsonFormat(shape =  JsonFormat.Shape.NUMBER_INT)
    private Duration duration;
}
