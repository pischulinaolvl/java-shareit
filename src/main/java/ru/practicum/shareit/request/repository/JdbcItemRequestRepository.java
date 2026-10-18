package ru.practicum.shareit.request.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.dto.ItemRequestRow;
import ru.practicum.shareit.request.mapper.ItemRequestRowMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserRowMapper;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcItemRequestRepository implements ItemRequestRepository {
    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ItemRequestRowMapper itemRequestRowMapper = new ItemRequestRowMapper();
    private final UserRowMapper userRowMapper = new UserRowMapper();

    @Override
    public ItemRequest findItemRequestById(Long id) {
        Map<String, Object> params = new HashMap<>();
        ItemRequestRow itemRequestRow;
        User user = null;

        params.put("id", id);

        String sql = "SELECT * FROM item_request WHERE id = :id";

        try {
            itemRequestRow = namedParameterJdbcTemplate.queryForObject(sql, params, itemRequestRowMapper);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }

        if (itemRequestRow.getRequestor() != null) {
            params = new HashMap<>();
            params.put("id", itemRequestRow.getRequestor());

            sql = "SELECT * FROM \"user\" WHERE id = :id";

            try {
                user = namedParameterJdbcTemplate.queryForObject(sql, params, userRowMapper);
            } catch (org.springframework.dao.EmptyResultDataAccessException e) {
                return null;
            }
        }

        return ItemRequest.builder()
                    .id(itemRequestRow.getId())
                    .description(itemRequestRow.getDescription())
                    .requestor(user)
                    .created(itemRequestRow.getCreated())
                    .build();
    }
}
