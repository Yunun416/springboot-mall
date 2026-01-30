package com.yun.springbootmall.controller;

import com.yun.springbootmall.dto.OrderRequest;
import com.yun.springbootmall.model.Order;
import com.yun.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<Order> createOrder(@PathVariable Integer userId, @RequestBody OrderRequest orderRequest){
        orderRequest.setUserId(userId);
        Integer orderId = orderService.createOrder(orderRequest);
        Order resOrder = orderService.findOrderById(orderId);

        return ResponseEntity.status(HttpStatus.OK).body(resOrder);

    }

    @GetMapping("/users/{userId}/orders/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Integer userId, @PathVariable Integer orderId){
        Order resOrder = orderService.findOrderById(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(resOrder);
    }

}
