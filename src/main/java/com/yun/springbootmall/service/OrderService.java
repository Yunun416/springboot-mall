package com.yun.springbootmall.service;

import com.yun.springbootmall.dto.OrderRequest;
import com.yun.springbootmall.model.Order;

public interface OrderService {
    public Integer createOrder(OrderRequest orderRequest);

    public Order findOrderById(Integer orderId);
}
