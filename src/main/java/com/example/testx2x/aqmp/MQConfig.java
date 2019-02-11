package com.example.testx2x.aqmp;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jiangyunxiong on 2018/5/29.
 *
 * 配置bean
 */
@Configuration
public class MQConfig {
    public static final String QUEUE_Caller = "queue_Caller";

    /**
     * Direct模式 交换机Exchange
     * 发送者先发送到交换机上，然后交换机作为路由再将信息发到队列，
     * */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_Caller, true);
    }



}
