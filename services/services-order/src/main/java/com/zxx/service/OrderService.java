package com.zxx.service;


import com.zxx.order.Order;

public interface OrderService {
    Order createOrder(Long product, Long userId);
}
