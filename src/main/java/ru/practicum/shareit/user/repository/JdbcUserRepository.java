package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.mapper.UserRowMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final UserRowMapper userRowMapper = new UserRowMapper();

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя обязательно");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email обязателен");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("name", user.getName());
        params.put("email", user.getEmail());

        String sql = "INSERT INTO \"user\" (name, email) VALUES (:name, :email)";

        namedParameterJdbcTemplate.update(sql, params);

        String selectSql = "SELECT * FROM \"user\" WHERE email = :email";

        return namedParameterJdbcTemplate.queryForObject(
                selectSql,
                params,
                userRowMapper
        );
    }

    @Override
    public int removeUser(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        String sql = "DELETE FROM \"user\" WHERE id = :id";
        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("Нельзя обновить пользователя без ID");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("name", user.getName());
        params.put("email", user.getEmail());
        params.put("id", user.getId());

        String sql = "UPDATE \"user\" SET name = :name, email = :email WHERE id = :id";

        int rowsAffected = namedParameterJdbcTemplate.update(sql, params);

        if (rowsAffected == 0) {
            throw new IllegalArgumentException("Пользователь с ID " + user.getId() + " не найден");
        }

        return user;
    }

    @Override
    public User findUserById(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        String sql = "SELECT * FROM \"user\" WHERE id = :id";

        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, userRowMapper);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM \"user\"";
        return namedParameterJdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public User findUserByEmail(String email) {
        String sql = "SELECT * FROM \"user\" WHERE email = :email";

        try {
            Map<String, Object> params = Collections.singletonMap("email", email);
            return namedParameterJdbcTemplate.queryForObject(sql, params, new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}