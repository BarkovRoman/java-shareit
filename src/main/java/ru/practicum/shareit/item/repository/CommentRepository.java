package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.dto.CommentShortResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<CommentShortResponseDto> findCommentByItem_Id(Long itemId);

    List<CommentShortResponseDto> findCommentByAuthor_Id(Long itemId);

    List<Comment> findByItemIn(List<Item> items, Sort created);
}
