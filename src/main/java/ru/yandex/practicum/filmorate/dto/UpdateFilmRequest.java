package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateFilmRequest {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private List<Integer> genreIds; // список id жанров
    private Integer mpaId;          // именно id рейтинга
    private Duration duration;

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
        return genreIds != null && !genreIds.isEmpty();
    }

    public boolean hasMpa() {
        return mpaId != null;
    }

    public boolean hasDuration() {
        return duration != null && !duration.isNegative() && !duration.isZero();
    }
}
