package com.zxx.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 使用@ConfigurationProperties注解来批量读取配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "order") // 配置批量绑定在nacos下，可以无需 @RefreshScope 就能实现自动刷新
public class OrderProperties {

    String timeout;
    String autoConfirm;  // 短横线写法auto-confirm 映射为驼峰命名
    String dbUrl;
}
