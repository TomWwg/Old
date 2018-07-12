package com.sws.mq.comsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class MyConsumerSchedule implements ApplicationContextAware{
    private Logger log = LoggerFactory.getLogger(MyConsumerSchedule.class);
    private Object[] arguments;
    private static ApplicationContext applicationContext;
    private class MqStartThread extends Thread{
      public void run(){
        for (Object beanName : arguments)
          try {
            IConsumer consumer = (IConsumer)applicationContext.getBean((String)beanName);
            consumer.start();
            log.debug("========================= " + beanName + " start success======================");
          }
          catch (Exception e) {
              log.error("启动MQ监听服务失败：", e);
              log.debug("========================= " + beanName + " start failed======================");
          }
      }
    }
    
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        log.debug("=========================MyConsumerSchedule go to start consumer======================");
        MyConsumerSchedule.applicationContext=ctx;
        MqStartThread mqStartThread = new MqStartThread();
        mqStartThread.start();
        log.debug("=========================MyConsumerSchedule finished======================");
      }
    
    public Object[] getArguments() {
        return arguments;
    }
    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    
     
}
