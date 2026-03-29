package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "likes")
public class Like {
    @EmbeddedId
    private LikeId id;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("filmId")
    @JoinColumn(name = "film_id")
    private Film film;
}

