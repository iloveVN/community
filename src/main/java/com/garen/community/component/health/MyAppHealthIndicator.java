package com.garen.community.component.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * 自定义健康状态指示器
 * 1. 编写一个指示器， 实现HealthIndicator 接口
 * 2. 指示器的名字 xxxHealthIndicator
 * 3. 加入容器中
 */
@Component
public class MyAppHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {

        // 自定义检查方法
//        return null;
//        return Health.up().build();  // 代表健康
        return Health.down().withDetail("msg", "服务异常").build();
    }
}
