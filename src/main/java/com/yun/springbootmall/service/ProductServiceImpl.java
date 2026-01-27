package com.yun.springbootmall.service;

import com.yun.springbootmall.dao.ProductDao;
import com.yun.springbootmall.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    public Product findById(Integer id){
        return productDao.findById(id);
    };
}
