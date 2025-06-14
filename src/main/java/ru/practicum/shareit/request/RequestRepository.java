package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestorIdOrderByCreatedDesc(Long id);
    List<ItemRequest> findAllByRequestorIdNot(Long requestorId);

}
