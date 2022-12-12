package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.dto.CommentShortResponseDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<CommentShortResponseDto> findCommentByItem_Id(Long itemId);

    List<CommentShortResponseDto> findCommentByAuthor_Id(Long itemId);
}
