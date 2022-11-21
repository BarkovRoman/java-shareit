package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAll();

    Optional<User> getBuId(long id);

    User add(User user);

    boolean update(User user, long id);

    boolean delete(long id);
}
