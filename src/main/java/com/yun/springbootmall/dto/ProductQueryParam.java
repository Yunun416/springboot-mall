package com.yun.springbootmall.dto;

import com.yun.springbootmall.constant.ProductCategory;

public class ProductQueryParam {
    String search;
    ProductCategory category;
    Integer starPrice;
    Integer endPrice;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public Integer getStarPrice() {
        return starPrice;
    }

    public void setStarPrice(Integer starPrice) {
        this.starPrice = starPrice;
    }

    public Integer getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(Integer endPrice) {
        this.endPrice = endPrice;
    }
}
