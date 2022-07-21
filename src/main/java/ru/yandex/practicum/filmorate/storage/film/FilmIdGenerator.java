package ru.yandex.practicum.filmorate.storage.film;

public class FilmIdGenerator {
    private long id;

    public long generateId() {
        return ++id;
    }
}
