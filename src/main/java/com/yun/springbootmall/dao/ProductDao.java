package com.yun.springbootmall.dao;


import com.yun.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {
    public Product findById(Integer id);
}
