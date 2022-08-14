package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service
public class UserService {
    private static final String INCORRECT_PARAMETER = "Некорректный параметр: %s = %d.";

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("friendshipDbStorage") FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(long id) {
        return userStorage.getUserById(id);
    }

    public void createFriendship(long userId, long friendId) {
        if (userId <= 0) {
            throw new UserNotFoundException(String.format(INCORRECT_PARAMETER, "id", userId));
        }

        if (friendId <= 0) {
            throw new UserNotFoundException(String.format(INCORRECT_PARAMETER, "friendId", friendId));
        }

        friendshipStorage.createFriendship(userId, friendId);
        log.debug(String.format("Пользователи с id %d и %d теперь друзья.", userId, friendId));
    }

    public void deleteFriendship(long userId, long friendId) {
        if (userId <= 0) {
            throw new ValidationException(String.format(INCORRECT_PARAMETER, "id", userId));
        }

        if (friendId <= 0) {
            throw new ValidationException(String.format(INCORRECT_PARAMETER, "friendId", friendId));
        }

        friendshipStorage.deleteFriendship(userId, friendId);
        log.debug(String.format("Пользователи с id %d и %d теперь не друзья.", userId, friendId));
    }

    public Collection<User> getFriends(long userId) {
        if (userId <= 0) {
            throw new ValidationException(String.format(INCORRECT_PARAMETER, "id", userId));
        }

        return friendshipStorage.getFriends(userId);
    }

    public Collection<User> getCommonFriends(long userId, long otherId) {
        if (userId <= 0) {
            throw new ValidationException(String.format(INCORRECT_PARAMETER, "id", userId));
        }

        if (otherId <= 0) {
            throw new ValidationException(String.format(INCORRECT_PARAMETER, "otherId", otherId));
        }

        return friendshipStorage.getCommonFriends(userId, otherId);
    }
}