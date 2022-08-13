package ru.yandex.practicum.filmorate.storage.filmgenres;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface FilmGenresStorage {
    void addFilmGenre(long filmId, long genreId);
    void deleteFilmGenres(long filmId);
    Set<Genre> getFilmGenres(long filmId);
}