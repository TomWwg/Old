package com.sws.mq.comsumer;

import java.io.ByteArrayInputStream;
import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.digester.Digester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.gk.essh.core.ServiceLocator;
import com.gk.essh.core.executor.ThreadExecutorService;
import com.sws.common.until.DateUtils;
import com.sws.model.WashHandLog;
import com.sws.service.WashHandLogService;

public class ConsumerQueueTask {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerQueueTask.class);
    private static BlockingQueue<QueueDataDto> queue = new LinkedBlockingQueue<QueueDataDto>();
    private static Hashtable<Integer, Long> dataNumber = new Hashtable<Integer, Long>();
    private static final int DATA_TYPE_WASH = 1;
    static {
        ThreadExecutorService.execute(queueTask());
        
    }

    private static Runnable queueTask() {
        return new Runnable() {
            public void run() {// 处理队列数据
                while (true) {
                    try {                     
                        QueueDataDto dto = queue.take();
                        if (dto.getType() == DATA_TYPE_WASH) {
                            WashHandLogService washHandLogService = ServiceLocator.findService("washHandLogService");
                            Digester digester = new Digester();
                            digester.setValidating(false);
                            digester.addObjectCreate("message", WashHandLog.class);
                            digester.addBeanPropertySetter("message/type");
                            digester.addBeanPropertySetter("message/eventType");
                            digester.addBeanPropertySetter("message/postCode");
                            digester.addBeanPropertySetter("message/hospitalNo");
                            digester.addBeanPropertySetter("message/departmentNo");
                            digester.addBeanPropertySetter("message/apNo");
                            digester.addBeanPropertySetter("message/deviceType");
                            digester.addBeanPropertySetter("message/deviceNo");
                            digester.addBeanPropertySetter("message/rfid");
                            digester.addBeanPropertySetter("message/rfidStatus");
                            digester.addBeanPropertySetter("message/reprotTime", "timeStr");
                            digester.addBeanPropertySetter("message/content");
                            digester.addBeanPropertySetter("message/ip");
                            WashHandLog washHand = (WashHandLog) digester.parse(new InputSource(
                                    new ByteArrayInputStream(dto.getDataXml().getBytes("utf-8"))));
                            washHand.setStatus(0L);
                            if (washHand.getTimeStr() != null) {
                                washHand.setCreateTime(DateUtils.str2DateByYMDHMS(washHand.getTimeStr()));
                            }
                            washHandLogService.proWashHand(washHand); //处理数据
                        } else {
                            logger.error("collectDataQueue has invalid data ");
                        }
                    } catch (Throwable e) {
                        logger.error("get data from Queue has Exception:{}", e);
                    }
                }
            }
        };
    }

    private static void setQueueData(int type, String dataXml) {
        if (queue.size() <= 100000) {
            QueueDataDto data = new QueueDataDto(type, dataXml);
            queue.add(data);
            //increateCount(type);
        } else {
            logger.warn("[collectDataQueue]there are too many trigger throw it.size{}", Integer.valueOf(queue.size()));
        }
    }

    @SuppressWarnings("unused")
    private static void increateCount(int type) {
        Long count = (Long) dataNumber.get(Integer.valueOf(type));
        dataNumber.put(Integer.valueOf(type), Long.valueOf(count != null ? count.longValue() + 1L : 1L));
    }

    @SuppressWarnings("unused")
    private static void decreaseCount(int type) {
        Long count = (Long) dataNumber.get(Integer.valueOf(type));
        if (count == null) {
            count = Long.valueOf(0L);
        }
        dataNumber.put(Integer.valueOf(type), Long.valueOf(count.longValue() > 0L ? count.longValue() - 1L : 0L));
    }

    public static void setWashQueue(String msg) {
        setQueueData(DATA_TYPE_WASH, msg);
    }

}
