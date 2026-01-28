package com.yun.springbootmall.service;

import com.yun.springbootmall.constant.ProductCategory;
import com.yun.springbootmall.dao.ProductDao;
import com.yun.springbootmall.dto.ProductQueryParam;
import com.yun.springbootmall.dto.ProductRequest;
import com.yun.springbootmall.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    public Product findById(Integer id){
        return productDao.findById(id);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        return productDao.createProduct(productRequest);
    }

    @Override
    public Integer updateProduct(Integer id, ProductRequest productRequest) {
        return productDao.updateProduct(id, productRequest);
    }

    @Override
    public void deleteProduct(Integer id) {
        productDao.deleteProduct(id);
    }

    @Override
    public List<Product> getProducts(ProductQueryParam productQueryParam) {
        return productDao.getProducts(productQueryParam);
    }


}
