package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    @Modifying
    @Query("update User u set u.name = ?1, u.email = ?2 where u.name is not null AND u.email is not null AND u.id = ?3")
    int updateNameAndEmailById(@Nullable String name, @Nullable String email, Long id);

    @Modifying
    @Query("update User u set u.name = ?1")
    int updateNameBy(@Nullable String name);

    @Modifying
    @Query("update User u set u.name = ?1 where u.name is not null AND u.id = ?2")
    int updateNameByNameNotNull(String name, Long id);

    /*List<User> getAll();

    Optional<User> getById(long id);

    User add(User user);

    User update(User user, long id);

    void delete(long id);*/
}
