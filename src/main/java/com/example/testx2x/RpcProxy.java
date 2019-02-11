package com.example.testx2x;

import com.alibaba.fastjson.JSON;

import com.example.testx2x.aqmp.CallServiceImpl;
import com.example.testx2x.aqmp.ServiceCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

@SuppressWarnings("unchecked")
@Service
public class RpcProxy {

@Autowired
ServiceCaller serviceCaller;


    String serviceVersion;



    public <T> T create(final Class<?> interfaceClass) {
        return create(interfaceClass, "");
    }

    public  <T> T create(final Class<?> interfaceClass, String version) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //todo 创建RPC请求，json化，扔进队列，hashmap记录 等待回复管道回复，回复getResponse对象

                        RpcRequest rpcRequest =new RpcRequest();
                        rpcRequest.setRequestId(UUID.randomUUID().toString());
                        rpcRequest.setInterfaceName(method.getDeclaringClass().getName());
                        rpcRequest.setServiceVersion(version);
                        rpcRequest.setParameterTypes(method.getParameterTypes());
                        rpcRequest.setParameters(args);
                        rpcRequest.setMethodName(method.getName());
                        String a =JSON.toJSONString(rpcRequest);

                        sendRequest(a);


                        return new CallServiceImpl().helloCall(1,2);
                    }
                }
        );
    }

    private void sendRequest(String a) {
        serviceCaller.sendUserBalanceInfoService(a);
    }
}
