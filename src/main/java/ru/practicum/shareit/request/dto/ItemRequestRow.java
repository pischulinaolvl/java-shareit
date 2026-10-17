package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public class ItemRequestRow {
    private Long id;
    private String description;
    private Long requestor;
    private LocalDateTime created;
}
