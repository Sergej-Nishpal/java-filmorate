package ru.yandex.practicum.filmorate.storage.filmgenres;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

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
        log.debug("Добавляем фильму с id = {} жанр с id = {}", filmId, genreId);
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }

    @Override
    public void deleteFilmGenre(long filmId, long genreId) {
        final String sqlQuery = "DELETE FROM FILM_GENRES WHERE FILM_ID = ? AND GENRE_ID = ?";
        log.debug("Удаляем у фильма с id = {} жанр с id = {}", filmId, genreId);
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }

    @Override
    public Set<Genre> getFilmGenres(long filmId) {
        final String sqlQuery = "SELECT G.GENRE_ID, G.GENRE_NAME FROM FILM_GENRES FG " +
                                "LEFT OUTER JOIN GENRES G ON FG.GENRE_ID = G.GENRE_ID " +
                                "WHERE FILM_ID = ?";
        log.debug("Запрашиваем жанры фильма с id = {}", filmId);
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenresSet, filmId);
    }

    private Set<Genre> mapRowToGenresSet(ResultSet rs) throws SQLException {
        Set<Genre> genresSet = new HashSet<>();
        while (rs.next()) {
            genresSet.add(new Genre(rs.getLong("GENRE_ID"), rs.getString("GENRE_NAME")));
        }
        return genresSet;
    }
}
