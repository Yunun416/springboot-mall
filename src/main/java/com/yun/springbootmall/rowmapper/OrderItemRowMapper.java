package com.yun.springbootmall.rowmapper;

import com.yun.springbootmall.model.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemRowMapper implements RowMapper<OrderItem> {
    @Override
    public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(rs.getInt("o.order_id"));
        orderItem.setOrderItemId(rs.getInt("o.order_item_id"));
        orderItem.setProductId(rs.getInt("o.product_id"));
        orderItem.setQuantity(rs.getInt("o.quantity"));
        orderItem.setAmount(rs.getInt("o.amount"));
        orderItem.setProductName(rs.getString("p.product_name"));
        orderItem.setImageUrl(rs.getString("p.image_url"));
        return orderItem;
    }
}
