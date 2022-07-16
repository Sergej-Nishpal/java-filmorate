package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate CINEMA_BORN_DATE = LocalDate.of(1895, 12, 28);

    private final Map<Long, Film> films = new HashMap<>();
    private final FilmIdGenerator filmIdGenerator = new FilmIdGenerator();

    @Override
    public Collection<Film> getAllFilms() {
        log.debug("Текущее количество сохранённых фильмов: {}", films.size());
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        validate(film);
        film.setId(filmIdGenerator.generateId());
        films.put(film.getId(), film);
        log.debug("Фильм {} добавлен.", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Фильм с id {} не найден.", film.getId());
            throw new ValidationException("Фильм с id = " + film.getId()
                    + " не найден и не может быть обновлён!");
        }

        validate(film);
        films.put(film.getId(), film);
        return film;
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BORN_DATE)) {
            log.error("Фильм \"{}\" не прошёл валидацию!", film.getName());
            throw new ValidationException("Указана некорректная дата релиза фильма!");
        }
    }
}
