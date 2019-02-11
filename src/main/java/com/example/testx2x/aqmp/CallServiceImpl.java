package com.example.testx2x.aqmp;


import com.example.testx2x.CallService;
import lombok.extern.slf4j.Slf4j;

@RpcService(value = CallService.class,version = "CallService1")
@Slf4j
public class CallServiceImpl implements CallService {
    @Override
    public String helloCall(int a, int b) {
        log.info("called!");
        return "good";
    }
}
