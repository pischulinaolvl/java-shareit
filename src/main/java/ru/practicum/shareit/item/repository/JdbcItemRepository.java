package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemRowMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcItemRepository  implements ItemRepository {
    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ItemRowMapper rowMapper = new ItemRowMapper();

    @Override
    public Item createItem(Item item) {
        String sql = "INSERT INTO item (name, description, available, owner_id, request_id) VALUES (:name, :description, :available, :owner, :request)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", item.getName());
        params.addValue("description", item.getDescription());
        params.addValue("available", item.isAvailable());
        if (item.getOwner() != null) {
            params.addValue("owner", item.getOwner().getId());
        } else {
            params.addValue("owner", null);
        }
        if (item.getItemRequest() != null) {
            params.addValue("request", item.getItemRequest().getId());
        } else {
            params.addValue("request", null);
        }

        namedParameterJdbcTemplate.update(sql, params, keyHolder);

        Long newItemId = keyHolder.getKey().longValue();
        if (newItemId == null) {
            throw new NotFoundException("Не удалось получить ID созданного режиссера");
        }

        return findItemById(newItemId);
    }

    @Override
    public int removeItem(Long id) {
        String sql = "DELETE FROM item WHERE id = :id";
        return namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
    }

    @Override
    public Item updateItem(Item item) {
        String sql = "UPDATE item SET name = :name, description = :description, available = :available WHERE id = :id";
        var params = new MapSqlParameterSource()
                .addValue("name", item.getName())
                .addValue("description", item.getDescription())
                .addValue("available", item.isAvailable())
                .addValue("id", item.getId());

        namedParameterJdbcTemplate.update(sql, params);
        return findItemById(item.getId());
    }

    @Override
    public Item findItemById(Long id) {
        String sql = "SELECT * FROM item WHERE id = :id";
        return namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource("id", id), rowMapper);
    }

    @Override
    public List<Item> getItems() {
        return namedParameterJdbcTemplate.query("SELECT * FROM item", new MapSqlParameterSource(), rowMapper);
    }

    @Override
    public List<Item> findItemByOwner(Long ownerId) {
        String sql = "SELECT * FROM item WHERE owner_id = :ownerId";
        return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource("ownerId", ownerId), rowMapper);
    }

    @Override
    public List<Item> getAvailableItems() {
        return namedParameterJdbcTemplate.query("SELECT * FROM item WHERE available = true", new MapSqlParameterSource(), rowMapper);
    }

    @Override
    public List<Item> findBySearchText(String text) {
        String sql = "SELECT * FROM item i " +
                "WHERE (LOWER(i.name) LIKE :text OR LOWER(i.description) LIKE :text) " +
                "AND i.available = true";

        MapSqlParameterSource params = new MapSqlParameterSource();

        if (text != null && !text.isBlank()) {
            params.addValue("text", "%" + text.toLowerCase() + "%");
        } else {
            params.addValue("text", "%%");
        }

        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
