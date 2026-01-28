package com.yun.springbootmall.controller;

import com.yun.springbootmall.constant.ProductCategory;
import com.yun.springbootmall.dto.ProductQueryParam;
import com.yun.springbootmall.dto.ProductRequest;
import com.yun.springbootmall.model.Product;

import com.yun.springbootmall.service.ProductService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> readProduct(@PathVariable Integer productId){
        Product product = productService.findById(productId);

        if (product != null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> readProducts(@RequestParam(required = false) String search,
                                                      @RequestParam(required = false) ProductCategory category,
                                                      @RequestParam(required = false) Integer starPrice,
                                                      @RequestParam(required = false) Integer endPrice,
                                                      @RequestParam(defaultValue = "created_date") String orderBy,
                                                      @RequestParam(defaultValue = "DESC") String sort){

        ProductQueryParam productQueryParam = new ProductQueryParam();
        productQueryParam.setSearch(search);
        productQueryParam.setCategory(category);
        productQueryParam.setStarPrice(starPrice);
        productQueryParam.setEndPrice(endPrice);
        productQueryParam.setOrderBy(orderBy);
        productQueryParam.setSort(sort);

        List<Product> productList = productService.getProducts(productQueryParam);

        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        Integer productId = productService.createProduct(productRequest);
        Product product = productService.findById(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest ){

        Product product = productService.findById(productId);

        if (product != null){
            Integer resproductId = productService.updateProduct(productId, productRequest);
            Product resProduct = productService.findById(resproductId);
            return ResponseEntity.status(HttpStatus.OK).body(resProduct);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Integer productId){

        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
