package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class NewFilmRequest {
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;

    private MpaDto mpa;
    private List<GenreDto> genres;

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public boolean hasDescription() {
        return description != null && !description.isBlank();
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }

    public boolean hasGenre() {
        return genres != null && !genres.isEmpty();
    }

    public boolean hasDuration() {
        return duration > 0;
    }
}


