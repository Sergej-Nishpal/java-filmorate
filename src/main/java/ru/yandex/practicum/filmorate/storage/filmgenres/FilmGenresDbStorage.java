package ru.yandex.practicum.filmorate.storage.filmgenres;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FilmGenresDbStorage implements FilmGenresStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenresDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFilmGenre(long filmId, long genreId) {
        final String sqlQuery = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery,filmId, genreId);
    }

    @Override
    public void deleteFilmGenre(long filmId, long genreId) {
        final String sqlQuery = "DELETE FROM FILM_GENRES WHERE FILM_ID = ? AND GENRE_ID = ?";
        jdbcTemplate.update(sqlQuery,filmId, genreId);
    }
}
