package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

@Slf4j
@Service
public class UserService {
    private static final String USER_ID_NOT_FOUND = "Пользователь с id %d не найден";
    private static final String INCORRECT_PARAMETER = "Некорректный параметр: %s = %d.";

    @Autowired
    private UserStorage userStorage;

    public void makeFriends(long userId, long friendId) {
        if (userId <= 0) {
            throw new IncorrectParameterException(String.format(INCORRECT_PARAMETER, "id", userId));
        }

        if (friendId <= 0) {
            throw new IncorrectParameterException(String.format(INCORRECT_PARAMETER, "friendId", friendId));
        }

        userStorage.findAllUsers()
                .stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_ID_NOT_FOUND, userId)))
                .getFriends()
                .add(friendId);

        userStorage.findAllUsers()
                .stream()
                .filter(user -> user.getId().equals(friendId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_ID_NOT_FOUND, friendId)))
                .getFriends()
                .add(userId);

        log.debug(String.format("Пользователи с id %d и %d теперь друзья.", userId, friendId));
    }

    public void unfriends(long userId, long friendId) {
        if (userId <= 0) {
            throw new IncorrectParameterException(String.format(INCORRECT_PARAMETER, "id", userId));
        }

        if (friendId <= 0) {
            throw new IncorrectParameterException(String.format(INCORRECT_PARAMETER, "friendId", friendId));
        }

        userStorage.findAllUsers()
                .stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_ID_NOT_FOUND, userId)))
                .getFriends()
                .remove(friendId);

        userStorage.findAllUsers()
                .stream()
                .filter(user -> user.getId().equals(friendId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_ID_NOT_FOUND, friendId)))
                .getFriends()
                .remove(userId);

        log.debug(String.format("Пользователи с id %d и %d теперь не друзья.", userId, friendId));
    }

    public Collection<User> getFriends(long userId) {
        if (userId <= 0) {
            throw new IncorrectParameterException(String.format(INCORRECT_PARAMETER, "id", userId));
        }

        return userStorage.getUserById(userId).getFriends()
                .stream()
                .map(friendId -> userStorage.getUserById(friendId))
                .collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(long userId, long otherId) {
        if (userId <= 0) {
            throw new IncorrectParameterException(String.format(INCORRECT_PARAMETER, "id", userId));
        }

        if (otherId <= 0) {
            throw new IncorrectParameterException(String.format(INCORRECT_PARAMETER, "otherId", otherId));
        }

        return userStorage.getUserById(userId).getFriends()
                .stream()
                .map(friendId -> userStorage.getUserById(friendId))
                .collect(Collectors.toList())
                .stream()
                .filter(user ->
                        userStorage.getUserById(otherId).getFriends()
                        .stream()
                        .map(otherFriendId -> userStorage.getUserById(otherFriendId))
                        .collect(Collectors.toList())
                                .contains(user))
                .collect(Collectors.toList());
    }
}
