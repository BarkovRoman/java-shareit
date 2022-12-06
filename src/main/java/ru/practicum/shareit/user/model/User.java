package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

//@Data
@AllArgsConstructor
@Setter @Getter @ToString
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;

    public User() {

    }
}
