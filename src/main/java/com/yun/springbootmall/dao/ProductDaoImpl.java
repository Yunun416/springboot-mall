package com.yun.springbootmall.dao;

import com.yun.springbootmall.model.Product;
import com.yun.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao{

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Product findById(Integer id) {
        String sql = "SELECT product_id, product_name, category, image_url, price, " +
                        "stock, description, created_date, last_modified_date " +
                        "FROM product " +
                        "WHERE product_id = :id";

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        List<Product> query = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        //找第一個，沒找到回傳 null
        return query.stream().findFirst().orElse(null);
    }
}
