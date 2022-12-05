package ru.practicum.shareit.item.model;

import lombok.*;
import org.hibernate.annotations.Table;
import ru.practicum.shareit.request.ItemRequest;

import javax.persistence.*;

//@Data
@Getter @Setter @ToString
@AllArgsConstructor
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean available; // статус доступности

    //private ItemRequest request; // ссылка на запрос создания вещи другого пользователя
    @Column(name = "owner_id")
    private Long owner;

    public Item() {

    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        return id != null && id.equals(((Item) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }*/
}
