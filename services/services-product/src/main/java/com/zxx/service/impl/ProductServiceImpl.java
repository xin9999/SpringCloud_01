package com.zxx.service.impl;

import com.zxx.product.Product;
import com.zxx.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public Product getProductById(Long id) {
        Product product = new Product();
        product.setId(id);
        product.setProductName("华为手机");
        product.setPrice(new BigDecimal(1000));
        product.setNum(100);
        // 模拟被调用方出现异常
//        throw new RuntimeException("被调用方出现异常");

/*        // 模拟API超时
        try {
            Thread.sleep(1000 * 2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return product;*/


        return product;
    }
}
