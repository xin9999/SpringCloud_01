package com.atguigu.order.feign;
// 67 - seata打通远程调用

    import org.springframework.cloud.openfeign.FeignClient;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestParam;

    @FeignClient(value = "seata-account")
    public interface AccountFeignClient {
        /**
         * 扣减账户余额
         * @return
         */
        @GetMapping("/debit")
        String debit(@RequestParam("userId") String userId,
                            @RequestParam("money") int money);
    }
