package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
public class LikeId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "film_id")
    private Long filmId;
}
