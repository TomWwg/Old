package com.sws.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gk.essh.core.ServiceLocator;
import com.gk.essh.core.executor.ThreadExecutorService;
import com.sws.common.entity.CmdData;
import com.sws.common.until.ByteUtil;
import com.sws.common.until.DateUtils;
import com.sws.model.ParameterInfo;
import com.sws.service.ParameterInfoService;

public class SocketQueueTask {
    private static final Logger logger = LoggerFactory.getLogger(SocketQueueTask.class);
    private static BlockingQueue<CmdData> sendQueue = new LinkedBlockingQueue<CmdData>();
    private static Hashtable<Integer, Long> dataNumber = new Hashtable<Integer, Long>();
    //private static BlockingQueue<CmdData> RecQueue = new LinkedBlockingQueue<CmdData>();
    private static Map<Integer, CmdData> recMap = new HashMap<Integer, CmdData>();// 结束后返回的字节 <命令包序号,返回字节>
    private static Socket socket;
    private static String serverIp;
    private static Integer serverPort;
    private static DataInputStream recStream;//接收数据流(收件箱)
    private static DataOutputStream sendStream; //发送数据流(发件箱)
    static {
        ThreadExecutorService.execute(queueTask());
        
    }

    private static void resetSocket(){
    	ParameterInfoService parameterInfoService = ServiceLocator.findService("parameterInfoService");
		ParameterInfo ipInfo = parameterInfoService.findByTypeAndKey(2, "ip");
		ParameterInfo portInfo = parameterInfoService.findByTypeAndKey(2, "port");
		if(ipInfo!=null&&ipInfo.getValue()!=null){
			serverIp = ipInfo.getValue();
		}
		if(portInfo!=null&&portInfo.getValue()!=null){
			serverPort = Integer.valueOf(portInfo.getValue());
		}
		try {
			socket = new Socket(serverIp, serverPort);
			recStream = new DataInputStream(socket.getInputStream()); 
			sendStream = new DataOutputStream(socket.getOutputStream()); 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(recStream != null) {
				try {
					recStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(sendStream != null) {
				try {
					sendStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}		
			}	
			recMap.clear();
			logger.error("resetSocket():socket close!");
			resetSocket();//socket启动
		}  
    }
    private static Runnable queueTask() {
        return new Runnable() {
            public void run() {// 处理队列数据
            	resetSocket();//socket启动
				byte[] recBuf;
				while (true) {
					try {
						CmdData cmdBuf = sendQueue.take();
						sendStream.write(cmdBuf.getData());
						sendStream.flush();//发送 
						recBuf = new byte[13];
						recStream.read(recBuf); //接收 
						//解析recBuf，清除相应的数据列
						if(recBuf.length==13){//返回的08报文，目前两种执行成功或者失败：00或者其他
							CmdData cmd = new CmdData();
							Integer seq = ByteUtil.byteToInt(recBuf, 2, 2);
							cmd.setSeq(seq);
							cmd.setData(recBuf);
							recMap.put(seq, cmd);
						}
					} catch (Throwable e) {
						logger.error("get data from Queue has Exception:{}", e);
					}finally {
						if(recStream != null) {
	    					try {
	    						recStream.close();
	    					} catch (IOException e) {
	    						e.printStackTrace();
	    					}
	    				}
						if(sendStream != null) {
	    					try {
	    						sendStream.close();
	    					} catch (IOException e) {
	    						e.printStackTrace();
	    					}
	    				}
						if(socket != null) {
							try {
								socket.close();
							} catch (IOException e) {
								e.printStackTrace();
							}		
						}	
						recMap.clear();
						logger.error("queueTask()：socket close!");
						resetSocket();//socket启动
					}
				}
            	
              
            }
        };
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
    
    public static byte[] time6Byte() {
		Date date = new Date();
		byte[] time = new byte[6];
		time[0]=Int2BCD(DateUtils.getYear(date)-2000);
		time[1]=Int2BCD(DateUtils.getMonth(date)+1);
		time[2]=Int2BCD(DateUtils.getDay(date));
		time[3]=Int2BCD(DateUtils.getHour(date));
		time[4]=Int2BCD(DateUtils.getMin(date));
		time[5]=Int2BCD(DateUtils.getSecond(date));
		return time;
	}
	private static byte Int2BCD(int i) {
		byte b = 0;
		int move = 0;
		while (i > 0) {
			b = (byte) (b | (i % 10) << move);
			i /= 10;
			move += 4;
		}
		return b;
	}
	
	public static void setQueueData(byte[] data,Integer seq, byte funCode, byte[] apNo) {
		byte[] dataSum = new byte[37];
		System.arraycopy(ByteUtil.intToByte2(37), 0, dataSum, 0, 2);
		System.arraycopy(ByteUtil.intToByte2(seq), 0, dataSum, 2, 2);
		dataSum[4] = funCode;
		System.arraycopy(apNo, 0, dataSum, 5, 4);
		System.arraycopy(data, 0, dataSum, 9, 28);// 数据从新封装
		CmdData sendData = new CmdData();
		sendData.setSeq(seq);
		sendData.setData(dataSum);
		sendData.setSendDate(new Date());
		if (sendQueue.size() <= 100000) {
			sendQueue.add(sendData);
			// increateCount(type);
		} else {
			logger.warn("[collectDataQueue]there are too many trigger throw it.size{}",
					Integer.valueOf(sendQueue.size()));
		}
    }
	public static byte[] getResultBySeq(Integer seq) {// 根据命令包序号
		Long start = System.currentTimeMillis();
		byte[] result = null;
		CmdData cmd = new CmdData();
		while (true) {
			try {
				Thread.sleep(500);// 0.5秒间隔运行
				Long end = System.currentTimeMillis();
				if (end - start > 25000) {// 60秒超时时间
					result=backError(seq);//通讯失败
					break;
				}
				cmd = recMap.get(seq);
				if(cmd!=null&&cmd.getData()!=null){
					result = cmd.getData();
					recMap.remove(seq);//返回值取出后，从返回队列清除
					break;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	private static byte[] backError(Integer seq) {
		byte[] back = new byte[13];
		for(byte i=0;i<13;i++){
			back[i]=0x0;
		}
		System.arraycopy(ByteUtil.intToByte2(13),0, back,0,2);
		System.arraycopy(ByteUtil.intToByte2(seq),0, back,2,2);
		System.arraycopy(ByteUtil.intToByte2(1),0, back,4,2);
		back[6]=0x08;
		back[11]=0x03;
		return back;
	}
    

}
