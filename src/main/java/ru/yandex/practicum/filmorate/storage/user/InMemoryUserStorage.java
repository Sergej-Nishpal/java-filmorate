package ru.yandex.practicum.filmorate.storage.user;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private static final String SPACE_CHARACTER = " ";
    private static final String INCORRECT_PARAMETER = "Некорректный параметр %s = %d.";

    private final Map<Long, User> users = new HashMap<>();
    private final UserIdGenerator userIdGenerator = new UserIdGenerator();

    @Override
    public Collection<User> findAllUsers() {
        log.debug("Текущее количество сохранённых пользователей: {}", users.size());
        return users.values();
    }

    @Override
    public User createUser(User user) {
        validate(user);
        user.setId(userIdGenerator.generateId());

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.debug("Добавлен пользователь: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь с id {} не найден.", user.getId());
            throw new UserNotFoundException("Пользователь с id = " + user.getId()
                    + " не найден и не может быть обновлён!");
        }

        users.put(user.getId(), user);
        log.debug("Обновлен пользователь с id = {}", user.getId());
        return user;
    }

    @Override
    public User getUserById(long id) {
        if (id <= 0) {
            log.error("Передан некорректный id = {}.", id);
            throw new UserNotFoundException(String.format(INCORRECT_PARAMETER, "id", id));
        }

        return users.get(id);
    }

    private void validate(User user) {
        if (user.getLogin().contains(SPACE_CHARACTER)) {
            log.error("Пользователь с логином \"{}\" не прошёл валидацию.", user.getLogin());
            throw new ValidationException("Логин не должен содержать пробелы!");
        }
    }
}
