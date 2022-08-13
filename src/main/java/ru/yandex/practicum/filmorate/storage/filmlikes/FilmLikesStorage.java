package ru.yandex.practicum.filmorate.storage.filmlikes;

public interface FilmLikesStorage {
    void addLike(long filmId, long userId);
    void deleteLike(long filmId, long userId);
    int getLikesCount(long filmId);
}
