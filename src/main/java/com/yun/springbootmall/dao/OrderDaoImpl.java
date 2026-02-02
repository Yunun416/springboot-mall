package com.yun.springbootmall.dao;

import com.yun.springbootmall.dto.OrderItemRequest;
import com.yun.springbootmall.dto.OrderRequest;
import com.yun.springbootmall.model.Order;
import com.yun.springbootmall.model.OrderItem;
import com.yun.springbootmall.rowmapper.OrderItemRowMapper;
import com.yun.springbootmall.rowmapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OrderDaoImpl implements OrderDao{
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Order findOrderById(Integer orderId) {

        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date" +
                " FROM `order`" +
                " WHERE 1=1" +
                " AND order_id = :order_id"
                ;

        Map<String, Object> map = new HashMap<>();
        map.put("order_id", orderId);

        List<Order> resOrderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        return resOrderList.stream().findFirst().orElse(null);
    }

    @Override
    public List<OrderItem> findOrderItemsByOrderId(Integer orderId) {
        String sql = "SELECT o.order_id, o.order_item_id, o.product_id, o.quantity, o.amount," +
                    " p.product_name, p.image_url" +
                    " FROM order_item o" +
                    " LEFT JOIN product p ON o.product_id = p.product_id " +
                    " WHERE 1=1 " +
                    " AND o.order_id = :orderId"
                    ;

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        System.out.println("orderId:"+orderId);
        System.out.println("sql: "+sql);

        return namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());
    }

    @Override
    public Integer createOrder(OrderRequest orderRequest) {
        String sql = "INSERT INTO `order` (user_id, total_amount, created_date, last_modified_date)" +
                " VALUES (:user_id, :total_amount, :created_date, :last_modified_date)"
                ;
        //Order
        Integer totalAmount = orderRequest.getTotalAmount();
        Integer userId = orderRequest.getUserId();

        Date now = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("total_amount", totalAmount);
        map.put("created_date", now);
        map.put("last_modified_date", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        Integer orderId = keyHolder.getKey().intValue();

        return orderId;
    }

    @Override
    public void createOrderItems(Integer orderId, OrderRequest orderRequest) {

        String sql  = "INSERT INTO order_item (order_id, product_id, quantity, amount)" +
                " VALUES(" +
                " " + orderId +
                " , :product_id, :quantity, :amount)"
                ;

        List<OrderItemRequest> orderItemList = orderRequest.getOrderItemList();

        MapSqlParameterSource[] mapSqlParameterSources = new MapSqlParameterSource[orderItemList.size()];

        for (int i = 0; i < orderItemList.size(); i++){
            OrderItemRequest orderItemRequest = orderItemList.get(i);
            mapSqlParameterSources[i] = new MapSqlParameterSource();
            mapSqlParameterSources[i].addValue("product_id", orderItemRequest.getProductId());
            mapSqlParameterSources[i].addValue("quantity", orderItemRequest.getQuantity());
            mapSqlParameterSources[i].addValue("amount", orderItemRequest.getAmount());
        }
        namedParameterJdbcTemplate.batchUpdate(sql, mapSqlParameterSources);
    }


}

