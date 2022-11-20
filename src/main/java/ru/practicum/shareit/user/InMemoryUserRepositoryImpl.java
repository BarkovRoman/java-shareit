package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ExistingEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;
@Repository
public class InMemoryUserRepositoryImpl implements UserRepository{
    private final Map<Long, User> users = new HashMap<>();
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
        String email = user.getEmail();
        long count = users.values().stream()
                .map(User::getEmail)
                .filter(f -> f.equals(email))
                .count();
        if(count == 0) {
            long id = getId();
            user.setId(id);
            users.put(id, user);
            return user;
        }
        throw new ExistingEmailException(String.format("User с email = %s уже существует", email));
    }

    @Override
    public boolean update(User user) {
        long id = user.getId();
        if(users.containsKey(id)) {
            users.put(id, user);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(long id) {
        if(users.containsKey(id)) {
            users.remove(id);
            return true;
        }
        return false;
    }

    private long getId() {
        return users.values().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0) + 1;

    }
}
