package com.sws.mq.comsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sws.common.until.StringUtil;

public class DeviceConsumer extends BaseConsumer implements IConsumer{
	 private Logger log = LoggerFactory.getLogger(DeviceConsumer.class);
	  public void receiveMsg(String notify)
	  {
		log.debug("=============DeviceConsumer receiveMsg start=======================");
		log.debug("=============notify:" + notify + "=======================");
	    if (StringUtil.isBlank(notify)) {
	    	log.info("receiveMsg is null");
	      return;
	    }else{
	        ConsumerQueueTask.setWashQueue(notify);
	    }
	    log.debug("=============DeviceConsumer receiveMsg end=======================");
	  }
}
