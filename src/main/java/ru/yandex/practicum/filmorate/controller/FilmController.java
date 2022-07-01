package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/films")
@Slf4j
public class FilmController {
    private static final LocalDate CINEMA_BORN_DATE = LocalDate.of(1895, 12, 28);

    private final Map<Long, Film> films = new HashMap<>();
    private final FilmIdGenerator filmIdGenerator = new FilmIdGenerator();

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.debug("Текущее количество сохранённых фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping
    @SuppressWarnings("UnusedReturnValue")
    public Film createFilm(@Valid @RequestBody Film film) {
        validate(film);
        film.setId(filmIdGenerator.generateId());
        films.put(film.getId(), film);
        log.debug("Фильм {} добавлен.", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Фильм с id {} не найден.", film.getId());
            throw new ValidationException("Фильм с id = " + film.getId()
                    + " не найден и не может быть обновлён!");
        }

        validate(film);
        films.put(film.getId(), film);
        return film;
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BORN_DATE)) {
            log.warn("Фильм \"{}\" не прошёл валидацию!", film.getName());
            throw new ValidationException("Указана некорректная дата релиза фильма!");
        }
    }
}

class FilmIdGenerator {
    private long id;

    long generateId() {
        return ++id;
    }
}
