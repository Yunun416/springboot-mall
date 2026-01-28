package com.yun.springbootmall.service;

import com.yun.springbootmall.constant.ProductCategory;
import com.yun.springbootmall.dto.ProductQueryParam;
import com.yun.springbootmall.dto.ProductRequest;
import com.yun.springbootmall.model.Product;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ProductService {
    public Product findById(Integer id);

    public Integer createProduct(ProductRequest productRequest);

    public Integer updateProduct(Integer id, ProductRequest productRequest);

    public void deleteProduct(Integer id);

    public List<Product> getProducts(ProductQueryParam productQueryParam);

}

