package ru.yandex.practicum.filmorate.service;

public class FilmIdGenerator {
    private long id;

    public long generateId() {
        return ++id;
    }
}
