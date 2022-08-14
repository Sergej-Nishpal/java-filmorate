package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FriendshipDbStorageTest {
    private final FriendshipDbStorage friendshipStorage;

    @Test
    @Order(1)
    void testGetFriends() {
        final long userId = 2; // у пользователя с id = 2
        final int expectedFriendQty = 1; // сейчас 1 друг
        final int actualFriendQty = friendshipStorage.getFriends(userId).size();
        assertThat(actualFriendQty)
                .isEqualTo(expectedFriendQty);
    }

    @Test
    @Order(2)
    void testAddFriend() {
        final long userId = 2;
        final long friendId = 1; // добавим пользователю с id = 2 друга с id = 1
        friendshipStorage.createFriendship(userId, friendId);
        final int expectedFriendQtyAfterAddingFriend = 2;
        final int actualFriendQtyAfterAddingFriend = friendshipStorage.getFriends(userId).size();
        assertThat(actualFriendQtyAfterAddingFriend)
                .isEqualTo(expectedFriendQtyAfterAddingFriend);
    }

    @Test
    @Order(3)
    void testDeleteFriend() {
        final long userId = 2;
        final long friendId = 1; // удалим у пользователя с id = 2 друга с id = 1
        friendshipStorage.deleteFriendship(userId, friendId);
        final int expectedFriendQtyAfterDeleteFriend = 1;
        final int actualFriendQtyAfterDeleteFriend = friendshipStorage.getFriends(userId).size();
        assertThat(actualFriendQtyAfterDeleteFriend)
                .isEqualTo(expectedFriendQtyAfterDeleteFriend);
    }

    @Test
    @Order(4)
    void testGetCommonFriends() {
        final long userId = 1; // у пользователя с id = 1
        final long otherId = 2; // и у пользователя с id = 2
        final long expectedCommonFriendId = 3; // сейчас есть один общий друг с id = 3

        final List<User> commonFriendsList = new ArrayList<>(friendshipStorage.getCommonFriends(userId, otherId));
        final int expectedCommonFriendsSize = 1;
        final int actualCommonFriendsSize = commonFriendsList.size();
        assertThat(actualCommonFriendsSize)
                .isEqualTo(expectedCommonFriendsSize);

        final long actualCommonFriendId = commonFriendsList.get(0).getId();
        assertThat(actualCommonFriendId)
                .isEqualTo(expectedCommonFriendId);
    }
}