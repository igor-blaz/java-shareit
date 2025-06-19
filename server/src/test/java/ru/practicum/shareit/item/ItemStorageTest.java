package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemStorageTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemStorage itemStorage;

    @Test
    void addItemTest() {
        Item item = new Item();
        when(itemRepository.save(item)).thenReturn(item);

        Item savedItem = itemStorage.addItem(item);

        assertEquals(item, savedItem);
        verify(itemRepository).save(item);
    }

    @Test
    void getItemByRequestIdTest() {
        Long requestId = 1L;
        List<Item> items = List.of(new Item());

        when(itemRepository.findAllByRequestId(requestId)).thenReturn(items);

        List<Item> result = itemStorage.getItemByRequestId(requestId);

        assertEquals(items, result);
        verify(itemRepository).findAllByRequestId(requestId);
    }

    @Test
    void addCommentTest() {
        Comment comment = new Comment();
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment savedComment = itemStorage.addComment(comment);

        assertEquals(comment, savedComment);
        verify(commentRepository).save(comment);
    }

    @Test
    void getCommentsTest() {
        Long itemId = 1L;
        List<Comment> comments = List.of(new Comment());

        when(commentRepository.findAllByItemId(itemId)).thenReturn(comments);

        List<Comment> result = itemStorage.getComments(itemId);

        assertEquals(comments, result);
        verify(commentRepository).findAllByItemId(itemId);
    }

    @Test
    void getItemTest() {
        Long id = 1L;
        Item item = new Item();
        item.setId(id);

        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        Item result = itemStorage.getItem(id);

        assertEquals(item, result);
        verify(itemRepository).findById(id);
    }

}
