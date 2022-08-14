package ru.yandex.practicum.filmorate.storage.film;

import java.util.Set;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Collection;
import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Mpa;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.storage.filmgenres.FilmGenresStorage;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {
    private static final LocalDate CINEMA_BORN_DATE = LocalDate.of(1895, 12, 28);
    private static final String INCORRECT_PARAMETER = "Некорректный параметр %s = %d.";
    private static final String FILM_ID_COLUMN_NAME = "FILM_ID";
    private static final String FILM_NAME_COLUMN_NAME = "FILM_NAME";
    private static final String FILM_DESCRIPTION_COLUMN_NAME = "DESCRIPTION";
    private static final String FILM_RELEASE_DATE_COLUMN_NAME = "RELEASE_DATE";
    private static final String FILM_DURATION_COLUMN_NAME = "DURATION";
    private static final String MPA_ID_COLUMN_NAME = "MPA_ID";
    private static final String MPA_NAME_COLUMN_NAME = "MPA_NAME";

    private final JdbcTemplate jdbcTemplate;
    private final FilmGenresStorage filmGenresStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmGenresStorage filmGenresStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmGenresStorage = filmGenresStorage;

    }

    @Override
    public Collection<Film> getAllFilms() {
        final String sqlQuery = "SELECT * FROM FILMS LEFT JOIN MPAS M on M.MPA_ID = FILMS.MPA_ID";
        log.debug("Запрашиваем все фильмы из БД.");
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film createFilm(Film film) {
        validate(film);
        SimpleJdbcInsert insertFilm = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns(FILM_ID_COLUMN_NAME);
        final long filmId = insertFilm.executeAndReturnKey(film.toMap()).longValue();
        film.setId(filmId);
        log.debug("Добавлен фильм \"{}\" (новый id: {})", film.getName(), filmId);
        addFilmGenres(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        checkIfFilmExists(film.getId());
        validate(film);
        final String sqlQuery =
                "UPDATE FILMS SET " +
                        "FILM_NAME = ?, " +
                        "DESCRIPTION = ?, " +
                        "RELEASE_DATE = ?, " +
                        "DURATION = ?, " +
                        "MPA_ID = ? WHERE FILM_ID = ?";
        final int recordsUpdated =
                jdbcTemplate.update(sqlQuery,
                        film.getName(),
                        film.getDescription(),
                        film.getReleaseDate(),
                        film.getDuration(),
                        film.getMpa().getId(),
                        film.getId());
        log.debug("Фильмов обновлено: {}", recordsUpdated);
        deleteFilmGenres(film);
        addFilmGenres(film);
        return film;
    }

    @Override
    public Film getFilmById(long filmId) {
        if (filmId <= 0) {
            log.error("Передан некорректный id = {}.", filmId);
            throw new FilmNotFoundException(String.format(INCORRECT_PARAMETER, "id", filmId));
        }

        try {
            final String sqlQuery = "SELECT * FROM FILMS LEFT JOIN MPAS M ON M.MPA_ID = FILMS.MPA_ID WHERE FILM_ID = ?";
            log.debug("Запрашиваем фильм с id {}.", filmId);
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
        } catch (EmptyResultDataAccessException e) {
            log.error("Фильм с id {} не найден.", filmId);
            throw new FilmNotFoundException("Фильм с id = " + filmId + " не найден.");
        }
    }

    private void checkIfFilmExists(long filmId) {
        getFilmById(filmId);
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BORN_DATE)) {
            log.error("Фильм \"{}\" не прошёл валидацию!", film.getName());
            throw new ValidationException("Указана некорректная дата релиза фильма!");
        }
    }

    private void addFilmGenres(final Film film) {
        final Set<Genre> filmGenres = film.getGenres();
        if (!filmGenres.isEmpty()) {
            for (Genre genre : filmGenres) {
                filmGenresStorage.addFilmGenre(film.getId(), genre.getId());
            }
            log.debug("Добавлены жанры для фильма \"{}\".", film.getName());
        } else {
            log.debug("У фильма \"{}\" нет жанров!", film.getName());
        }
    }

    private void deleteFilmGenres(final Film film) {
        filmGenresStorage.deleteFilmGenres(film.getId());
        log.debug("Удалены все жанры фильма \"{}\".", film.getName());
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong(FILM_ID_COLUMN_NAME))
                .name(rs.getString(FILM_NAME_COLUMN_NAME))
                .description(rs.getString(FILM_DESCRIPTION_COLUMN_NAME))
                .releaseDate(rs.getDate(FILM_RELEASE_DATE_COLUMN_NAME).toLocalDate())
                .duration(rs.getInt(FILM_DURATION_COLUMN_NAME))
                .mpa(new Mpa(rs.getLong(MPA_ID_COLUMN_NAME), rs.getString(MPA_NAME_COLUMN_NAME)))
                .genres(filmGenresStorage.getFilmGenres(rs.getLong(FILM_ID_COLUMN_NAME)))
                .build();
    }
}
