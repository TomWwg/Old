package com.sws.common;

public class SeqsGenerater {
	private int cmdSeq = 0x8000;
	private static SeqsGenerater seqsGenerater = null;

	private SeqsGenerater() {
	}

	/**
	 * 取得PrimaryGenerater的单例实现
	 *
	 * @return
	 */
	public static SeqsGenerater getInstance() {
		if (seqsGenerater == null) {
			synchronized (SeqsGenerater.class) {
				if (seqsGenerater == null) {
					seqsGenerater = new SeqsGenerater();
				}
			}
		}
		return seqsGenerater;
	}

	/**
	 * 生成下一个编号
	 */
	public synchronized int getCmdSeq() {
		if (cmdSeq == 0xFFFF) {
			cmdSeq = 0x8000;
		} else {
			cmdSeq++;
		}
		return cmdSeq;
	}

}
