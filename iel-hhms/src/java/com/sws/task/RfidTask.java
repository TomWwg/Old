package com.sws.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gk.essh.core.task.AbstractTaskEntity;
import com.sws.common.until.ByteUtil;
import com.sws.dao.DeviceInfoDao;
import com.sws.model.DeviceInfo;
import com.sws.service.SocketService;

public class RfidTask extends AbstractTaskEntity{
	@Autowired
    private SocketService socketService;
	@Autowired
    private DeviceInfoDao deviceInfoDao;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		byte[] dataSum_bed = new byte[28];
		byte[] dataSum_sx = new byte[28];
		byte[] dataSum_rfid = new byte[28];
		byte[] time = new byte[6];
		byte[] apNo = new byte[4];
		byte[] ini={0x00,0x00,0x00};
		byte[] bed10={0x10,(byte) 0xff,(byte) 0xff};
		byte[] sx29={0x29,(byte) 0xff,(byte) 0xff};
		byte[] rfid40={0x40,(byte) 0xff,(byte) 0xff,(byte) 0xff};
		List<Integer> seqs = new ArrayList<Integer>();
		List<byte[]> sendData = new ArrayList<byte[]>();
		time=ByteUtil.time6Byte();
		for(byte i=0;i<28;i++){
			dataSum_bed[i]=0x0;
			dataSum_sx[i]=0x0;
			dataSum_rfid[i]=0x0;
		}
		dataSum_bed[27]=0x1;
		dataSum_bed[0]=(byte) 0x90;
		dataSum_bed[1]=(byte) 0x01;//前置机巡检设备
		dataSum_sx[27]=0x1;
		dataSum_sx[0]=(byte) 0x90;
		dataSum_sx[1]=(byte) 0x01;//前置机巡检设备
		dataSum_rfid[27]=0x1;
		dataSum_rfid[0]=(byte) 0x90;
		dataSum_rfid[1]=(byte) 0x01;//前置机巡检设备
		System.arraycopy(time, 0, dataSum_bed, 2, 6);
		System.arraycopy(time, 0, dataSum_sx, 2, 6);
		System.arraycopy(time, 0, dataSum_rfid, 2, 6);
		System.arraycopy(bed10, 0, dataSum_bed, 19, 3);
		sendData.add(dataSum_bed);//床识别器
		System.arraycopy(sx29, 0, dataSum_sx, 19, 3);
		sendData.add(dataSum_sx);//速效识别器
		System.arraycopy(rfid40, 0, dataSum_rfid, 22, 4); //胸牌
		sendData.add(dataSum_rfid);
		List<DeviceInfo> apList = deviceInfoDao.findBy("type", "38");
		for(DeviceInfo ap:apList){
			if(ap.getNo()!=null){
				apNo = ByteUtil.intToByte4(Integer.valueOf(ap.getNo()));
				seqs=socketService.send(sendData, ByteUtil.GET_STATE, apNo);//发送
				for(Integer seq:seqs){
					byte[] back = socketService.getResultBySeq(seq);
					if (back != null) {

					}
				}
			}
		}
		
		
	}

}
