package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests", schema = "public")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String description;    // текст запроса, содержащий описание требуемой вещи
    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor;    // пользователь, создавший запрос
    private LocalDateTime created = LocalDateTime.now();    // дата и время создания запроса.
}
