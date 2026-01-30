package com.yun.springbootmall.service;

import com.yun.springbootmall.dao.OrderDao;
import com.yun.springbootmall.dto.OrderItemRequest;
import com.yun.springbootmall.dto.OrderRequest;
import com.yun.springbootmall.model.Order;
import com.yun.springbootmall.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderServiceImpl implements OrderService{

    @Autowired
    OrderDao orderDao;

    private final static Logger log = LoggerFactory.getLogger(OrderService.class);

    @Transactional
    @Override
    public Integer createOrder(OrderRequest orderRequest) {
        int totalAmount = 0;

        //查商品庫存 & 價錢
        List<Product> productList = orderDao.findProductById(orderRequest);

        Map<String, Object> map = new HashMap<>();

        for (Product product : productList){
            Integer productId = product.getProductId();
            Integer productStock = product.getStock();
            Integer productPrice = product.getPrice();
            String productName = product.getProductName();
            map.put(productId + "@productStock", productStock);
            map.put(productId + "@productPrice", productPrice);
            map.put(productId + "@productName", productName);
        }

        System.out.println("map: "+map);

        //計算出其他項目的值後塞入 orderItemListRequest
        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItemList()){
            Integer orderProductId = orderItemRequest.getProductId();
            Integer orderQuantity = orderItemRequest.getQuantity();

            Integer productStock = (Integer) map.get(orderProductId + "@productStock");
            Integer productPrice = (Integer) map.get(orderProductId + "@productPrice");

            // 庫存不足
            if (productStock < orderQuantity){
                    log.warn("商品 {} 庫存不足", (String)map.get(orderProductId + "@productName"));
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            Integer orderAmount = orderQuantity * productPrice;
            orderItemRequest.setAmount(orderAmount);
            totalAmount += orderAmount;
        }

        orderRequest.setTotalAmount(totalAmount);
        Integer orderId = orderDao.createOrder(orderRequest);
        orderDao.createOrderItems(orderId, orderRequest);

        return orderId;

    }

    @Override
    public Order findOrderById(Integer orderId) {
        Order resOrder = orderDao.findOrderById(orderId);
        resOrder.setOrderItemList(orderDao.findOrderItemsByOrderId(orderId));
        return resOrder;
    }
}
