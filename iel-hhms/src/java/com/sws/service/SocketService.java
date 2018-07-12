package com.sws.service;

import java.util.List;

public interface SocketService {
	public List<Integer> send(List<byte[]> list,byte funCode,byte[] apNo);
	public byte[] time6Byte();
	public byte[] getResultBySeq(Integer seq);
}
