/*
package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User add(User user) {
        duplicationEmail(user);
        long id = getId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user, long id) {
        duplicationEmail(user);
        User userUpdate = users.get(id);
        if (user.getEmail() != null) {
            userUpdate.setEmail(user.getEmail());
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            userUpdate.setName(user.getName());
        }
        return userUpdate;
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }

    private long getId() {
        return ++id;
    }

    private void duplicationEmail(User user) {
        users.values().stream()
                .map(User::getEmail)
                .filter(f -> f.equals(user.getEmail()))
                .findFirst()
                .ifPresent((email) -> {
                    throw new RuntimeException(String.format("User с email = %s уже существует", email));
                });
    }
}
*/
