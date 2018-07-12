package com.sws.action;

import com.sws.common.baseAction.BaseAction;
import com.sws.serial.SerialBean;
import com.sws.serial.SerialData;

public class SerialAction extends BaseAction<SerialAction> {
	
	private static final long serialVersionUID = -2868312613376378663L;
	private SerialData serialData = new SerialData();
	/**************胸牌读写测试***********************/
	public String serialSet(){
		
		return "serialSet";
	}
	public void getOrgTree() {
		SerialBean SB = new SerialBean(3);
		SB.Initialize();
		//写数据 aa 55 14 90 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 08 66 7e
		byte[] dataSum = new byte[26];
		for(byte i=0;i<26;i++){
			dataSum[i]=0x0;
		}
		dataSum[0]=(byte) 0xaa;
		dataSum[1]=(byte) 0x55;
		dataSum[2]=(byte) 0x14;
		dataSum[3]=(byte) 0x90;
		dataSum[4]=(byte) 0x04;
		dataSum[23]=(byte) 0x08;
		dataSum[24]=(byte) 0x66;
		dataSum[25]=(byte) 0x7e;
		SB.WritePortByByte(dataSum);
		SB.ReadPortByte();
		SB.ClosePort();
	}
	
}
