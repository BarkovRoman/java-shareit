package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ExistingEmailException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    //private final List<User> users = new ArrayList<>();
    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getBuId(long id) {
        return users.values().stream()
                .filter(user -> user.getId() == id)
                .findFirst();
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
    public boolean update(User user, long id) {
        duplicationEmail(user);
        if (users.containsKey(id)) {
            users.get(id).setId(id);
            if (user.getEmail() != null) {
                users.get(id).setEmail(user.getEmail());
            }
            if (user.getName() != null) {
                users.get(id).setName(user.getName());
            }
            //users.put(id, user);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(long id) {
        if (users.containsKey(id)) {
            users.remove(id);
            return true;
        }
        return false;
    }

    private long getId() {
        return ++id;
    }

    private void duplicationEmail(User user) {
        long count = users.values().stream()
                .map(User::getEmail)
                .filter(f -> f.equals(user.getEmail()))
                .count();
        if (count != 0) {
            throw new ExistingEmailException(String.format("User с email = %s уже существует", user.getEmail()));
        }
    }
}
