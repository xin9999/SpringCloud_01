package com.zxx.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

// 由于现在使用的不是微服务，而是使用的是第三方API, 所有使用url
@FeignClient(value = "weather-client", url = "http://aliv18.data.moji.com")
public interface WeatherFeignClient {

    @PostMapping("/whapi/json/alicityweather/condition")
    String getWeather(
            @RequestHeader("Authorization") String appCode,
            @RequestParam("token") String token,
            @RequestParam("cityId") String cityId);
}
