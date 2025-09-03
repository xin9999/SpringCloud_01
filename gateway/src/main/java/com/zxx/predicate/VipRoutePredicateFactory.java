package com.zxx.predicate;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.cloud.gateway.handler.predicate.QueryRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
// 类的名称与配置文件中，predicate名称相对应
public class VipRoutePredicateFactory extends AbstractRoutePredicateFactory<VipRoutePredicateFactory.Config> {

    public VipRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                // localhost/search?q=haha&user=zhangxixin

                ServerHttpRequest request = serverWebExchange.getRequest();
                // 取出url中，参数名对应的值
                String first = request.getQueryParams().getFirst(config.param);

                // 注意此处。first.equals(config.value)
                return StringUtils.hasText(first) && first.equals(config.value);
            }
        };
    }

    // 传参的顺序
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("param","value");
    }

    // 以下两个参数，代表在yml配置中可以传两个参数
    @Validated
    public static class Config {
        @NotEmpty
        private String param;

        @NotEmpty
        private String value;

        public String getParam() {
            return this.param;
        }

        public void setParam(@NotEmpty String param) {
            this.param = param;
        }

        public @NotEmpty String getValue() {
            return value;
        }

        public void setValue(@NotEmpty String value) {
            this.value = value;
        }
    }
}
