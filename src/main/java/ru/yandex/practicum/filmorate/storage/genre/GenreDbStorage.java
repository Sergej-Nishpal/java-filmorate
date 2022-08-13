package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component
public class GenreDbStorage implements GenreStorage {
    private static final String INCORRECT_PARAMETER = "Некорректный параметр %s = %d";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Collection<Genre> getAllGenres() {
        final String sqlQuery = "SELECT * FROM GENRES ORDER BY GENRE_ID";
        log.debug("Запрашиваем все жанры из БД.");
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Genre getGenreById(long genreId) {
        if (genreId <= 0) {
            log.error("Передан некорректный id = {}.", genreId);
            throw new GenreNotFoundException(String.format(INCORRECT_PARAMETER, "id", genreId));
        }

        try {
            final String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
            log.debug("Запрашиваем жанр с id {}.", genreId);
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, genreId);
        } catch (EmptyResultDataAccessException e) {
            log.error("Жанр с id {} не найден.", genreId);
            throw new GenreNotFoundException("Жанр с id = " + genreId + " не найден.");
        }
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("GENRE_ID"))
                .name(rs.getString("GENRE_NAME"))
                .build();
    }
}
