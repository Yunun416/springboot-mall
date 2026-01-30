package com.yun.springbootmall.dao;

import com.yun.springbootmall.dto.OrderRequest;
import com.yun.springbootmall.model.Order;
import com.yun.springbootmall.model.OrderItem;
import com.yun.springbootmall.model.Product;

import java.util.List;

public interface OrderDao {
    public Integer createOrder(OrderRequest orderRequest);

    public List<Product> findProductById(OrderRequest orderRequest);

    public void createOrderItems(Integer orderId, OrderRequest orderRequest);

    public Order findOrderById(Integer orderId);

    public List<OrderItem> findOrderItemsByOrderId(Integer orderId);

}
