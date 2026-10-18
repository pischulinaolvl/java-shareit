package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User createUser(User user);

    int removeUser(Long id);

    User updateUser(User user);

    User findUserById(Long id);

    List<User> getUsers();

    User findUserByEmail(String email);
}
