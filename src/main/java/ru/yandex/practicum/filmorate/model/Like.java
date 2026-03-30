package ru.yandex.practicum.filmorate.model;

import lombok.Data;


@Data
public class Like {
    private LikeId id;
    private User user;

    private Film film;
}

