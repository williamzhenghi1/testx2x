package com.example.testx2x;

import com.example.testx2x.aqmp.ServiceCaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class serviceController {

    @Autowired
    ServiceCaller serviceCaller;

    @Autowired
    RpcProxy rpcProxy;

    @GetMapping("/call")
    String call()
    {

        CallService callService =  rpcProxy.create(CallService.class,"1");
        String a =callService.helloCall(1,2);
        log.info(a);

        return "1";
    }

}
