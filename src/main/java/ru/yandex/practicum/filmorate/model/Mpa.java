package ru.yandex.practicum.filmorate.model;

public enum Mpa {
    G(1, "G"),
    PG(2, "PG"),
    PG_13(3, "PG-13"),
    R(4, "R"),
    NC_17(5, "NC-17");

    private final Integer id;
    private final String name;

    Mpa(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Mpa fromId(int id) {
        for (Mpa mpa : values()) {
            if (mpa.id == id) {
                return mpa;
            }
        }
        throw new IllegalArgumentException("MPA с id=" + id + " не найден");
    }
}

