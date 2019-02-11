package com.example.testx2x.aqmp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ServiceCaller {

    @Autowired
   public AmqpTemplate amqpTemplate;


   public void sendUserBalanceInfoService(String task)
    {

        amqpTemplate.convertAndSend(MQConfig.QUEUE_Caller, task);
        log.info("task send!");
    }


}
