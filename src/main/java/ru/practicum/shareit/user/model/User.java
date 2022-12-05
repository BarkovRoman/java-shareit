package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

//@Data
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;

    public User() {

    }
}
