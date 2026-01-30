package com.yun.springbootmall.dao;


import com.yun.springbootmall.constant.ProductCategory;
import com.yun.springbootmall.dto.OrderRequest;
import com.yun.springbootmall.dto.ProductQueryParam;
import com.yun.springbootmall.dto.ProductRequest;
import com.yun.springbootmall.model.Product;

import java.util.List;
import java.util.Map;

public interface ProductDao {
    public Product findProductById(Integer id);

    public List<Product> findProductsById(OrderRequest orderRequest);

    public Integer createProduct(ProductRequest productRequest);

    public Integer updateProduct(Integer id, ProductRequest productRequest);

    public void updateProductStock(Map<Integer, Integer> mapRenewStock);

    public void deleteProduct(Integer id);

    public List<Product> getProducts(ProductQueryParam productQueryParam);

    public Integer countProducts(ProductQueryParam productQueryParam);

}
