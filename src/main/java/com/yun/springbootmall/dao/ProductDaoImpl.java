package com.yun.springbootmall.dao;

import com.yun.springbootmall.constant.ProductCategory;
import com.yun.springbootmall.dto.ProductQueryParam;
import com.yun.springbootmall.dto.ProductRequest;
import com.yun.springbootmall.model.Product;
import com.yun.springbootmall.rowmapper.ProductRowMapper;
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

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql = "INSERT INTO product (product_name, category, image_url, price, stock, "+
                "description, created_date, last_modified_date) " +
                "VALUES ( :product_name, :category, :image_url, :price, :stock, " +
                ":description, :created_date, :last_modified_date) ";

        Date now = new Date();

        Map<String, Object> map = new HashMap<>();
        map.put("product_name", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("image_url", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());
        map.put("created_date", now);
        map.put("last_modified_date", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int id = keyHolder.getKey().intValue();
        return id;
    }

    @Override
    public Integer updateProduct(Integer id, ProductRequest productRequest) {
        String sql = "UPDATE product " +
                "SET product_name = :product_name, " +
                "product_name = :product_name, " +
                "category = :category, " +
                "image_url = :image_url, " +
                "price = :price, " +
                "stock = :stock, " +
                "description = :description, " +
                "last_modified_date = :last_modified_date " +
                "WHERE product_id = :product_id";

        Date now = new Date();

        Map<String, Object> map = new HashMap<>();
        map.put("product_id", id);
        map.put("product_name", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("image_url", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());
        map.put("last_modified_date", now);

        namedParameterJdbcTemplate.update(sql, map);
        return id;
    }

    @Override
    public void deleteProduct(Integer id) {
        System.out.println("id:"+id);
        String sql = "DELETE FROM product "+
                "WHERE product_id = :product_id";

        Map<String, Object> map = new HashMap<>();
        map.put("product_id", id);

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public List<Product> getProducts(ProductQueryParam productQueryParam) {
        String sql = "SELECT product_id, product_name, category, image_url, price, " +
                "stock, description, created_date, last_modified_date " +
                "FROM product WHERE 1=1 ";

        Map<String, Object> map = new HashMap<>();

        String search = productQueryParam.getSearch();
        ProductCategory category = productQueryParam.getCategory();
        Integer starPrice = productQueryParam.getStarPrice();
        Integer endPrice = productQueryParam.getEndPrice();

        if (search != null){
            sql += "AND product_name LIKE :product_name ";
            map.put("product_name", "%" + search + "%");

        }

        if (category != null){
            sql += "AND category = :category ";
            map.put("category", category.name());
        }

        if (starPrice != null){
            sql += "AND price >= :starPrice ";
            map.put("starPrice", starPrice);
        }

        if (endPrice != null){
            sql += "AND price <= :endPrice ";
            map.put("endPrice", endPrice);
        }

        System.out.println("sql:"+sql);

        List<Product> query = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        return query;
    }
}
