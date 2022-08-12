package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;

public interface FriendshipStorage {
    void createFriendship(long userId, long friendId);
    void confirmFriendship(long userId, long friendId);
    void deleteFriendship(long userId, long friendId);
    Collection<User> getFriends(long userId);
    Collection<User> getCommonFriends(long userId, long otherId);
}
