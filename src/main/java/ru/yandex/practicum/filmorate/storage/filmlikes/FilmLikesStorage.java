package ru.yandex.practicum.filmorate.storage.filmlikes;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmLikesStorage {
    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    Collection<Film> getPopularFilms(int count);
}