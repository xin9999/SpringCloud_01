package com.zxx.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;


// 单只需要对一个服务使用特定过滤器时，可以使用自定义过滤器工厂
@Component
public class OnceTokenGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {


    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                // 每次相应之前，添加一个一次性令牌，支持 uuid, jwt 等各种格式

                return chain.filter(exchange).then(Mono.fromRunnable(()->{
                    ServerHttpResponse response = exchange.getResponse();
                    HttpHeaders httpHeaders = response.getHeaders();

                    String value = config.getValue();
                    if ("uuid".equalsIgnoreCase(value)){
                        value = UUID.randomUUID().toString();
                    }
                    if ("jwt".equalsIgnoreCase(value)){
                        value = "eyJzdWIiOiJ1c2VyLTEyMyIsIm5hbWUiOiJ0ZXN0Iiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MDAwMDAwMDAsImlhdCI6MTY5OTk2NDAwMH0";
                    }
                    httpHeaders.add(config.getName(),value);
                }));
            }
        };
    }
}
