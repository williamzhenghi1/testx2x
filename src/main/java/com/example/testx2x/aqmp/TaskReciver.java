package com.example.testx2x.aqmp;


import com.alibaba.fastjson.JSON;

import com.example.testx2x.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TaskReciver implements ApplicationContextAware {

    HashMap<String,Object> handlerMap =new HashMap<>();

    @RabbitListener(queues = MQConfig.QUEUE_Caller)
    void BigredPacketLogTaskMessageReciver(String message) throws Exception {
        log.info("Task recived!");
        RpcRequest rpcRequest =  JSON.parseObject(message,RpcRequest.class);

       Object result = handle(rpcRequest);
       log.info(String.valueOf(result));
       log.info("done");
    }

    private Object handle(RpcRequest request) throws Exception {
        // 获取服务对象
        String serviceName = request.getInterfaceName();
        String serviceVersion = request.getServiceVersion();

//        serviceName += "-" + serviceVersion;

        log.info("request.getInterFaceName:..."+serviceName);

        Object serviceBean = handlerMap.get(serviceName);

        if (serviceBean == null) {
            throw new RuntimeException(String.format("can not find service bean by key: %s", serviceName));
        }

        // 获取反射调用所需的参数
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        // 执行反射调用
//        Method method = serviceClass.getMethod(methodName, parameterTypes);
//        method.setAccessible(true);
//        return method.invoke(serviceBean, parameters);
        // 使用 CGLib 执行反射调用
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (!serviceBeanMap.isEmpty()) {
            for (Object serviceBean : serviceBeanMap.values()) {
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.value().getName();
                String serviceVersion = rpcService.version();
//                if (!StringUtil.isNullOrEmpty(serviceVersion)) {
//                    serviceName += "-" + serviceVersion;
//                }
                handlerMap.put(serviceName, serviceBean);
            }
        }
    }
}
