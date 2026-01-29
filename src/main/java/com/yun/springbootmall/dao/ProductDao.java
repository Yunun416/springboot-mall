package com.yun.springbootmall.dao;


import com.yun.springbootmall.constant.ProductCategory;
import com.yun.springbootmall.dto.ProductQueryParam;
import com.yun.springbootmall.dto.ProductRequest;
import com.yun.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {
    public Product findById(Integer id);

    public Integer createProduct(ProductRequest productRequest);

    public Integer updateProduct(Integer id, ProductRequest productRequest);

    public void deleteProduct(Integer id);

    public List<Product> getProducts(ProductQueryParam productQueryParam);

    public Integer countProducts(ProductQueryParam productQueryParam);

}
