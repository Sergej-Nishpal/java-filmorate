package ru.yandex.practicum.filmorate.storage.filmgenres;

public interface FilmGenresStorage {
    void addFilmGenre(long filmId, long genreId);
    void deleteFilmGenre(long filmId, long genreId);
}