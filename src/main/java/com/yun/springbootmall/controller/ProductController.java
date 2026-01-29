package com.yun.springbootmall.controller;

import com.yun.springbootmall.constant.ProductCategory;
import com.yun.springbootmall.dto.ProductQueryParam;
import com.yun.springbootmall.dto.ProductRequest;
import com.yun.springbootmall.model.Product;

import com.yun.springbootmall.service.ProductService;
import com.yun.springbootmall.util.QueryInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
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
    public ResponseEntity<QueryInfo<Product>> readProducts(
            //查詢條件 Filtering
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) Integer starPrice,
            @RequestParam(required = false) Integer endPrice,

            // 排序 Sorting
            @RequestParam(defaultValue = "created_date") String orderBy,
            @RequestParam(defaultValue = "DESC") String sort,

            // 分頁 Pagination
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "2") @Max(50) @Min(0) Integer limit){

        ProductQueryParam productQueryParam = new ProductQueryParam();
        productQueryParam.setSearch(search);
        productQueryParam.setCategory(category);
        productQueryParam.setStarPrice(starPrice);
        productQueryParam.setEndPrice(endPrice);
        productQueryParam.setOrderBy(orderBy);
        productQueryParam.setSort(sort);
        productQueryParam.setPage(page);
        productQueryParam.setLimit(limit);

        List<Product> productList = productService.getProducts(productQueryParam);
        Integer countProducts = productService.countProducts(productQueryParam);

        QueryInfo<Product> queryInfo = new QueryInfo<>();
        queryInfo.setResult(productList);
        queryInfo.setPage(page);
        queryInfo.setLimit(limit);
        queryInfo.setTotal(countProducts);

        return ResponseEntity.status(HttpStatus.OK).body(queryInfo);
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
            Integer resProductId = productService.updateProduct(productId, productRequest);
            Product resProduct = productService.findById(resProductId);
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
