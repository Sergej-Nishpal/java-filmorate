package ru.yandex.practicum.filmorate.storage.user;

public class UserIdGenerator {
    private long id;

    public long generateId() {
        return ++id;
    }
}
