package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(long filmId);

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    Collection<Film> getPopularFilms(int count);
}