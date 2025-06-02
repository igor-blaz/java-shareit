package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = "ru/practicum/shareit/item")
public class ItemStorage {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;


    public Item addItem(Item item) {
        log.info("!!!Добавили вещь {} {}", item.getId(), item);
        itemRepository.save(item);
        return item;
    }
    public Comment addComment(Comment comment){
        return commentRepository.save(comment);
    }


    public Item getItem(long id) {
        return itemRepository.findById(id)

                .orElseThrow(() -> {
                    log.warn("Юзер с id: {} не найден", id);
                    return new NotFoundException("Пользователь с id: " + id + "не найден");
                });

    }

    public List<Item> getItemsFromUser(Long userId) {
        return itemRepository.findAllByOwnerId(userId);
    }

    public List<Item> searchByText(String text) {

        return itemRepository.searchByNameAndDescription(text);
    }


}
