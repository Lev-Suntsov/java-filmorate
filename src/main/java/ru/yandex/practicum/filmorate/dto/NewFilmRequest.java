package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Data
public class NewFilmRequest {
    private String name;
    private String description;
    private LocalDate releaseDate;
    private List<Long> genreIds; // список id жанров
    private int mpaId;           // именно id рейтинга
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

    // mpaId — примитив, null быть не может, проверяем диапазон в сервисе

    public boolean hasDuration() {
        return duration != null && !duration.isNegative() && !duration.isZero();
    }
}

