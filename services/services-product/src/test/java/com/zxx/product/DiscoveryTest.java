package com.zxx.product;

import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.nacos.api.exception.NacosException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@SpringBootTest
public class DiscoveryTest {

    // 法一（Spring 的标准规范，无论使用什么注册中心都可以调用它的API）
    @Autowired
    DiscoveryClient discoveryClient;

    // 法二(这个是引入Nacos时，才能用的API)
    @Autowired
    NacosServiceDiscovery nacosServiceDiscovery;

    @Test
    void discoveryClientTest(){
        for (String service : discoveryClient.getServices()) {
            System.out.println("service=" + service);

            // 获取ip+port
            for (ServiceInstance instance : discoveryClient.getInstances(service)) {
                System.out.println("ip=" + instance.getHost() + "; port=" + instance.getPort() );
            }
        }
    }

    @Test
    void nacosDiscoveryClient() throws NacosException {
        for (String service : nacosServiceDiscovery.getServices()) {
            System.out.println("nas service=" + service);

            // 获取ip+port
            for (ServiceInstance instance : nacosServiceDiscovery.getInstances(service)) {
                System.out.println("ip=" + instance.getHost() + "; port=" + instance.getPort() );
            }
        }
    }
}
