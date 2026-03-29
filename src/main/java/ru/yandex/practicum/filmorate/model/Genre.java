package ru.yandex.practicum.filmorate.model;


public enum Genre {
    COMEDY(1, "Комедия"),
    DRAMA(2, "Драма"),
    CARTOON(3, "Мультфильм"),
    THRILLER(4, "Триллер"),
    DOCUMENTARY(5, "Документальный"),
    ACTION(6, "Боевик");

    private final Integer id;
    private final String name;

    Genre(int id, String name) {
        this.id = id;       // числовой id
        this.name = name;   // человекочитаемое имя
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Genre fromId(int id) {
        for (Genre g : values()) {
            if (g.id == id) {
                return g;
            }
        }
        throw new IllegalArgumentException("Жанр с id=" + id + " не найден");
    }
}
