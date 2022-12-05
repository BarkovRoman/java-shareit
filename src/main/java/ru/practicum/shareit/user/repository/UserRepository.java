package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query("update User u set u.name = :name1, u.email = :email2 where u.name = :name3 and u.email = :email4")
    int updateNameAndEmailByNameAndEmail(@NonNull @Param("name") String name, @NonNull @Param("email") String email, @NonNull @Param("name") String name1, @NonNull @Param("email") String email1);
    @Modifying
    @Query("update User u set u.name = :name1 where upper(u.name) = upper(:name2)")
    int updateNameByNameAllIgnoreCase(@Param("name") String name, @Param("name") String name1);
    @Override
    List<User> findAll();

    Optional<User> getById(long id);

    User update(User user);

    void delete(long id);

    /*List<User> getAll();

    Optional<User> getById(long id);

    User add(User user);

    User update(User user, long id);

    void delete(long id);*/
}
