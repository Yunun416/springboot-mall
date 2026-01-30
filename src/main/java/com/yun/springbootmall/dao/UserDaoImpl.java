package com.yun.springbootmall.dao;

import com.yun.springbootmall.dto.UserRequest;
import com.yun.springbootmall.model.User;
import com.yun.springbootmall.rowmapper.UserRowMapper;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao{

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createDao( UserRequest userRequest) {
        String sql = "INSERT INTO user (email, password, created_date, last_modified_date)" +
                    " VALUES (:email, :password, :created_date, :last_modified_date)";

        Map<String, Object> map = new HashMap<>();

        String email = userRequest.getEmail();
        String password = userRequest.getPassword();

        Date now = new Date();

        map.put("email", email);
        map.put("password", password);
        map.put("created_date", now);
        map.put("last_modified_date", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        int id = keyHolder.getKey().intValue();
        return id;
    }

    @Override
    public User findUserById(Integer id) {

        String sql = "SELECT user_id, email, password, created_date, last_modified_date"
                + " FROM  user"
                + " WHERE 1=1"
                + " AND user_id = :user_id";
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", id);

        List<User> query = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        return query.stream().findFirst().orElse(null);
    }

    @Override
    public User getUserEmail(String email) {

        String sql = "SELECT user_id, email, password, created_date, last_modified_date"
                + " FROM  user"
                + " WHERE 1=1"
                + " AND email = :email";
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<User> query = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        return query.stream().findFirst().orElse(null);
    }

    @Override
    public User login(UserRequest userRequest) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date" +
                " FROM  user" +
                " WHERE 1=1" +
                " AND email = :email";

        String email = userRequest.getEmail();
        String password = userRequest.getPassword();

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<User> query = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());
        return query.stream().findFirst().orElse(null);

    }
}
