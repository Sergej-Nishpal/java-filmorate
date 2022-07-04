package ru.yandex.practicum.filmorate.service;

public class UserIdGenerator {
    private long id;

    public long generateId() {
        return ++id;
    }
}
