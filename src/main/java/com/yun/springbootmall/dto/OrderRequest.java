package com.yun.springbootmall.dto;

import java.util.List;

public class OrderRequest {
    private List<OrderItemRequest> orderItemList;
    private Integer orderId;
    private Integer userId;
    private Integer totalAmount;

    public List<OrderItemRequest> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemRequest> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

}
