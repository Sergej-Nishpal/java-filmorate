package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/users")
@Slf4j
public class UserController {
    private static final String SPACE_CHARACTER = " ";

    private final Map<Long, User> users = new HashMap<>();
    private final UserIdGenerator userIdGenerator = new UserIdGenerator();

    @GetMapping
    public Collection<User> findAllUsers() {
        log.debug("Текущее количество сохранённых пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validate(user);
        user.setId(userIdGenerator.generateId());

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.debug("Добавлен пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь с id {} не найден.", user.getId());
            throw new ValidationException("Пользователь с id = " + user.getId()
                    + " не найден и не может быть обновлён!");
        }
        users.put(user.getId(), user);
        log.debug("Обновлен пользователь с id = {}", user.getId());
        return user;
    }

    private void validate(User user) {
        if (user.getLogin().contains(SPACE_CHARACTER)) {
            log.warn("Пользователь с логином \"{}\" не прошёл валидацию.", user.getLogin());
            throw new ValidationException("Логин не должен содержать пробелы!");
        }
    }
}

class UserIdGenerator {
    private long id;

    long generateId() {
        return ++id;
    }
}
