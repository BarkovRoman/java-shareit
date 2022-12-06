package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    //User update(User user);
    List<User> findByEmailContainingIgnoreCase(String emailSearch);
    /*List<User> getAll();

    Optional<User> getById(long id);

    User add(User user);

    User update(User user, long id);

    void delete(long id);*/
}
