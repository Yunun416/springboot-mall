package com.yun.springbootmall.dao;

import com.yun.springbootmall.constant.ProductCategory;
import com.yun.springbootmall.dto.OrderItemRequest;
import com.yun.springbootmall.dto.OrderRequest;
import com.yun.springbootmall.dto.ProductQueryParam;
import com.yun.springbootmall.dto.ProductRequest;
import com.yun.springbootmall.model.Product;
import com.yun.springbootmall.rowmapper.ProductRowMapper;
import com.yun.springbootmall.util.QueryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductDaoImpl implements ProductDao{

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Product findProductById(Integer id) {
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
    public List<Product> findProductsById(OrderRequest orderRequest) {

        String sql = "SELECT product_id, product_name, category, image_url, price," +
                " stock, description, created_date, last_modified_date" +
                " FROM product" +
                " WHERE 1=1" +
                " AND product_id IN(:productIds)"
                ;

        List<Integer> productIds = new ArrayList<>();

        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItemList()){
            Integer productId = orderItemRequest.getProductId();
            productIds.add(productId);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("productIds", productIds);

        return namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
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
    public void updateProductStock(Map<Integer, Integer> mapRenewStock) {
        Date now = new Date();
        String sql = "UPDATE product" +
                    " SET stock = :stock," +
                    " last_modified_date = :lastModifiedDate" +
                    " WHERE 1=1" +
                    " AND product_id = :productId"
                    ;

        MapSqlParameterSource[] mapSqlParameterSources = new MapSqlParameterSource[mapRenewStock.size()];

        int i = 0;
        for (Map.Entry<Integer, Integer> entry : mapRenewStock.entrySet()) {
            Integer productId = entry.getKey();
            Integer renewStock = entry.getValue();
            mapSqlParameterSources[i] = new MapSqlParameterSource();
            mapSqlParameterSources[i].addValue("productId", productId);
            mapSqlParameterSources[i].addValue("stock", renewStock);
            mapSqlParameterSources[i].addValue("lastModifiedDate", now);
            i++;
        }

        namedParameterJdbcTemplate.batchUpdate(sql, mapSqlParameterSources);
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
        String sql = "SELECT product_id, product_name, category, image_url, price" +
                " ,stock, description, created_date, last_modified_date" +
                " FROM product WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        String orderBy = productQueryParam.getOrderBy();
        String sort = productQueryParam.getSort();
        Integer page = productQueryParam.getPage();
        Integer limit = productQueryParam.getLimit();

        sql += addFilteringSql(productQueryParam, map);

        // 排序 Sorting
        sql += " ORDER BY " + orderBy + " " + sort;

        // 分頁 Pagination
        if (page.equals(1)){
            sql += " LIMIT " + limit;
        }else {
            Integer OFFSET = (page - 1) * limit;
            sql += " LIMIT " + limit + " OFFSET " + OFFSET;
        }

        System.out.println(sql);


        List<Product> query = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        return query;
    }

    @Override
    public Integer countProducts(ProductQueryParam productQueryParam) {
        String sql = "SELECT COUNT(*)" +
                " FROM product WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        sql += addFilteringSql(productQueryParam, map);

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
        return total;
    }

    //查詢條件 Filtering
    private String addFilteringSql(ProductQueryParam productQueryParam, Map<String, Object> map){
        String sqlWhere = "";
        String search = productQueryParam.getSearch();
        ProductCategory category = productQueryParam.getCategory();
        Integer starPrice = productQueryParam.getStarPrice();
        Integer endPrice = productQueryParam.getEndPrice();

        if (search != null){
            sqlWhere += " AND product_name LIKE :product_name";
            map.put("product_name", "%" + search + "%");
        }

        if (category != null){
            sqlWhere += " AND category = :category";
            map.put("category", category.name());
        }

        if (starPrice != null){
            sqlWhere += " AND price >= :starPrice";
            map.put("starPrice", starPrice);
        }

        if (endPrice != null){
            sqlWhere += " AND price <= :endPrice";
            map.put("endPrice", endPrice);
        }
        return sqlWhere;
    }
}
