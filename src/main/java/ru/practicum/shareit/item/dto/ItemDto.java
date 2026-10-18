package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.request.model.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
@EqualsAndHashCode
public class ItemDto {

    private Long id;

    private String name;
    private String description;
    private Boolean available;
    private ItemRequest itemRequest;
}