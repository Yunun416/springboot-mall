package com.yun.springbootmall.service;

import com.yun.springbootmall.dao.OrderDao;
import com.yun.springbootmall.dao.ProductDao;
import com.yun.springbootmall.dao.UserDao;
import com.yun.springbootmall.dto.OrderItemRequest;
import com.yun.springbootmall.dto.OrderRequest;
import com.yun.springbootmall.model.Order;
import com.yun.springbootmall.model.Product;
import com.yun.springbootmall.model.User;
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

    @Autowired
    ProductDao productDao;

    @Autowired
    UserDao userDao;

    private final static Logger log = LoggerFactory.getLogger(OrderService.class);

    @Transactional
    @Override
    public Integer createOrder(OrderRequest orderRequest) {
        // 確認是否註冊帳號
        Integer userId = orderRequest.getUserId();
        User user = userDao.findUserById(userId);

        if (user == null){
            log.warn("該 userId {} 不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;

        // 查商品庫存 & 價錢
        List<Product> productList = productDao.findProductsById(orderRequest);

        // 存查的商品現況
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

        // 存商品更新庫存數量
        Map<Integer, Integer> mapRenewStock = new HashMap<>();

        //計算出其他項目的值後塞入 orderItemListRequest
        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItemList()){
            Integer orderProductId = orderItemRequest.getProductId();
            Integer orderQuantity = orderItemRequest.getQuantity();

            Integer productStock = (Integer) map.get(orderProductId + "@productStock");
            Integer productPrice = (Integer) map.get(orderProductId + "@productPrice");

            // 庫存不足
            int renewStcok = productStock - orderQuantity;
            if (renewStcok < 0){
                    log.warn("商品 {} 庫存不足", (String)map.get(orderProductId + "@productName"));
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            Integer orderAmount = orderQuantity * productPrice;
            orderItemRequest.setAmount(orderAmount);
            totalAmount += orderAmount;

            // 放要更新庫存的值
            mapRenewStock.put(orderProductId, renewStcok);
        }

        productDao.updateProductStock(mapRenewStock);
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
