package com.sws.mq.comsumer;

import java.io.UnsupportedEncodingException;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import com.gk.sys.sws.core.mq.component.IMqComponent;
import com.gk.sys.sws.core.mq.component.MqComponentFactory;

public class BaseConsumer {
	private static final Logger log = LoggerFactory.getLogger(BaseConsumer.class);
	private SimpleMessageConverter eventReportConvert = new SimpleMessageConverter();
	private MqComponentFactory mqComponentFactory;
	private IMqComponent mqComponent;
	private String destionName;
	private boolean topicFlag;
	private BasicAcceptThread msgMdp;

	public IMqComponent getMqComponent() throws JMSException {
		if (this.mqComponent == null) {
			this.mqComponent = this.mqComponentFactory.createMqComponent(
					this.destionName, this.topicFlag);
			this.mqComponent.setBizListener(this);
			this.mqComponent.setMsgConverter(eventReportConvert);
			this.mqComponent.setMethodName("receiveMsg");
		}
		return this.mqComponent;
	}

	public void start() {
		try {
			getMqComponent().start();
			log.info("component[" + this.destionName + "] has start ");
		} catch (JMSException e) {
			log.error("启动监听器时出现异常", e);
		}
	}

	public void stop() {
		try {
			if (this.mqComponent != null)
				this.mqComponent.stop();
		} catch (Exception e) {
			log.error("停止监听器时出现异常", e);
		}
	}

	public void receiveMsg(Object message) {
		if ((message instanceof byte[]))
			receiveByteMsg((byte[]) message);
		else if ((message instanceof String))
			receiveStringMsg((String) message);
		else
			try {
				processMsg(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public void receiveStringMsg(String msg) {
		try {
			processMsg(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processMsg(Object msg) {
		try {
			if (this.msgMdp == null) {
				log.error("MsgMdp is null");
				return;
			}
			this.msgMdp.processMsg(msg);
		} catch (Exception e) {
			log.error("processMsg has Exception:", e);
		}
	}

	public void receiveByteMsg(byte[] msg) {
		try {
			log.debug("receiveMsg byte size:" + msg.length);

			String nofify = new String(msg, "UTF-8");

			receiveMsg(nofify);
		} catch (UnsupportedEncodingException e) {
			log.error("receiveMsg has Exception:" + e);
			e.printStackTrace();
		}
	}

	public SimpleMessageConverter getEventReportConvert() {
		return eventReportConvert;
	}

	public void setEventReportConvert(SimpleMessageConverter eventReportConvert) {
		this.eventReportConvert = eventReportConvert;
	}

	public MqComponentFactory getMqComponentFactory() {
		return mqComponentFactory;
	}

	public void setMqComponentFactory(MqComponentFactory mqComponentFactory) {
		this.mqComponentFactory = mqComponentFactory;
	}

	public String getDestionName() {
		return destionName;
	}

	public void setDestionName(String destionName) {
		this.destionName = destionName;
	}

	public boolean isTopicFlag() {
		return topicFlag;
	}

	public void setTopicFlag(boolean topicFlag) {
		this.topicFlag = topicFlag;
	}

	public BasicAcceptThread getMsgMdp() {
		return msgMdp;
	}

	public void setMsgMdp(BasicAcceptThread msgMdp) {
		this.msgMdp = msgMdp;
	}

	public void setMqComponent(IMqComponent mqComponent) {
		this.mqComponent = mqComponent;
	}
	
}
