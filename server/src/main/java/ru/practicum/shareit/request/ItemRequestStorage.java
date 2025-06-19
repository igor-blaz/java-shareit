package ru.practicum.shareit.request;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = "ru/practicum/shareit/request")
public class ItemRequestStorage {

    private final RequestRepository requestRepository;

    public ItemRequest saveItemRequest(ItemRequest itemRequest) {

        ItemRequest request = requestRepository.save(itemRequest);
        log.info("Сохранён запрос с ID = {}", request.getId());
        return request;
    }

    public List<ItemRequest> getItemRequestByUserId(Long id) {
        return requestRepository.findAllByRequestorIdOrderByCreatedDesc(id);
    }

    public ItemRequest getByRequestId(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("запроса не существует "));
    }

    public List<ItemRequest> getAllNotMine(Long userId) {
        return requestRepository.findAllByRequestorIdNot(userId);
    }
}
