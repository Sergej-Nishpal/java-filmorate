package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFriendshipStorage implements FriendshipStorage {
    private static final String USER_ID_NOT_FOUND = "Пользователь с id %d не найден";

    private final UserStorage userStorage;

    public InMemoryFriendshipStorage(@Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public void createFriendship(long userId, long friendId) {
        userStorage.findAllUsers()
                .stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_ID_NOT_FOUND, userId)))
                .getFriendshipStatus().keySet()
                .add(friendId);

        userStorage.findAllUsers()
                .stream()
                .filter(user -> user.getId().equals(friendId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_ID_NOT_FOUND, friendId)))
                .getFriendshipStatus().keySet()
                .add(userId);

        log.debug(String.format("Пользователи с id %d и %d теперь друзья.", userId, friendId));
    }

    @Override
    public void confirmFriendship(long userId, long friendId) {
        userStorage.findAllUsers()
                .stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_ID_NOT_FOUND, userId)))
                .getFriendshipStatus().put(friendId, true);
    }

    @Override
    public void deleteFriendship(long userId, long friendId) {
        userStorage.findAllUsers()
                .stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_ID_NOT_FOUND, userId)))
                .getFriendshipStatus().keySet()
                .remove(friendId);

        userStorage.findAllUsers()
                .stream()
                .filter(user -> user.getId().equals(friendId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_ID_NOT_FOUND, friendId)))
                .getFriendshipStatus().keySet()
                .remove(userId);

        log.debug(String.format("Пользователи с id %d и %d теперь не друзья.", userId, friendId));
    }

    @Override
    public Collection<User> getFriends(long userId) {
        return userStorage.getUserById(userId).getFriendshipStatus().keySet()
                .stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<User> getCommonFriends(long userId, long otherId) {
        return userStorage.getUserById(userId).getFriendshipStatus().keySet()
                .stream()
                .filter(userStorage.getUserById(otherId).getFriendshipStatus().keySet()::contains)
                .collect(Collectors.toList())
                .stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}