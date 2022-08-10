package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private static final String INCORRECT_PARAMETER = "Некорректный параметр %s = %d.";

    private final FilmStorage filmStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public void setLike(long filmId, long userId) {
        if (filmId <= 0) {
            throw new FilmNotFoundException(String.format(INCORRECT_PARAMETER, "id", filmId));
        }

        if (userId <= 0) {
            throw new FilmNotFoundException(String.format(INCORRECT_PARAMETER, "userId", userId));
        }

        filmStorage.getFilmById(filmId)
                .getLikes()
                .add(userId);
    }

    public void unlike(long filmId, long userId) {
        if (filmId <= 0) {
            throw new FilmNotFoundException(String.format(INCORRECT_PARAMETER, "id", filmId));
        }

        if (userId <= 0) {
            throw new FilmNotFoundException(String.format(INCORRECT_PARAMETER, "userId", userId));
        }

        filmStorage.getFilmById(filmId)
                .getLikes()
                .remove(userId);
    }

    public Collection<Film> getPopularFilms(int count) {
        if (count <= 0) {
            throw new FilmNotFoundException(String.format(INCORRECT_PARAMETER, "count", count));
        }

        return filmStorage.getAllFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
