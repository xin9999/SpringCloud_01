package com.atguigu.business.service.impl;

import com.atguigu.business.feign.OrderFeignClient;
import com.atguigu.business.feign.StorageFeignClient;
import com.atguigu.business.service.BusinessService;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BusinessServiceImpl implements BusinessService {
    @Autowired
    private StorageFeignClient storageFeignClient;  // 67 - seata打通远程调用
    @Autowired
    private OrderFeignClient orderFeignClient;  // 67 - seata打通远程调用

    @Override
    // 使用seata全局事务
    @GlobalTransactional  // 68 - 整合seata, 不启动则只有本地事务回滚（只要报错的当前服务会回滚），库存和账户数据不会回滚
    public void purchase(String userId, String commodityCode, int orderCount) {
        // 1. 扣减库存
        storageFeignClient.deduct(commodityCode, orderCount); // 67 - seata打通远程调用
        // 2. 创建订单
        orderFeignClient.create(userId, commodityCode, orderCount);// 67 - seata打通远程调用
    }
}
