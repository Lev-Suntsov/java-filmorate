package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = "id")
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;

    @JsonFormat(shape =  JsonFormat.Shape.NUMBER_INT)
    private Duration duration;
}
