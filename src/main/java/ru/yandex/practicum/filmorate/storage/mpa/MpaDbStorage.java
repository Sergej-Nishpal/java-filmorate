package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> getAllMpas() {
        final String sqlQuery = "SELECT * FROM MPAS";
        log.debug("Запрашиваем все MPA-рейтинги из БД.");
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public Mpa getMpaById(long mpaId) {

        try {
            final String sqlQuery = "SELECT * FROM MPAS WHERE MPA_ID = ?";
            log.debug("Запрашиваем MPA-рейтинг с id {}.", mpaId);
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, mpaId);
        } catch (EmptyResultDataAccessException e) {
            log.error("MPA-рейтинг с id {} не найден.", mpaId);
            throw new GenreNotFoundException("MPA-рейтинг с id = " + mpaId + " не найден.");
        }
    }

    private Mpa mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getLong("MPA_ID"))
                .name(rs.getString("MPA_NAME"))
                .build();
    }
}