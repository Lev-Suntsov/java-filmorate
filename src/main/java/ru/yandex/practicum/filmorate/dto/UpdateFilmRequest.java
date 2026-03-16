package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UpdateFilmRequest {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Integer rate;
    private Integer mpaId;
    private Set<Integer> genres;

    public boolean hasName() {
        return name != null;
    }

    public boolean hasDescription() {
        return  !(description == null || description.isBlank());
    }

    public boolean hasReleaseDate() {
        return  !(releaseDate == null);
    }

    public boolean hasGenre() {
        return !(genres == null);
    }

    public boolean hasMpa() {
        return !(mpaId == null);
    }

    public boolean hasDuration() {
        return !(duration == null);
    }
}
