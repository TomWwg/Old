package com.sws.mq.comsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BasicAcceptThread {
	  protected int queue_size = 100000;
	  protected boolean using_queue_cache = true;
	  protected boolean using_queue_cache_sec = false;
	  protected boolean isGetting = false;
	  protected BlockingQueue<Object> queue_cache = new ArrayBlockingQueue<Object>(this.queue_size);
	  protected BlockingQueue<Object> queue_cache_sec = new ArrayBlockingQueue<Object>(this.queue_size);

	  public void processMsg(Object msg) throws Exception
	  {
	    if (msg != null)
	      addMessage(msg);
	  }

	  private synchronized void addMessage(Object obj)
	    throws Exception
	  {
	    if (this.using_queue_cache) {
	      if (this.queue_cache.size() < this.queue_size) {
	        this.queue_cache.put(obj);
	      }
	    }
	    else if (this.queue_cache_sec.size() < this.queue_size)
	      this.queue_cache_sec.put(obj);
	  }

	  public synchronized void clearQueue()
	  {
	    if (!this.using_queue_cache)
	      this.queue_cache.clear();
	    else
	      this.queue_cache_sec.clear();
	  }

	  public synchronized void isGetting(boolean bln)
	  {
	    this.isGetting = bln;
	  }

	  public synchronized int getSizeQueue()
	  {
	    if (this.using_queue_cache) {
	      return this.queue_cache_sec.size();
	    }
	    return this.queue_cache.size();
	  }

	  public synchronized BlockingQueue<Object> getQueue()
	  {
	    if (this.using_queue_cache) {
	      return this.queue_cache_sec;
	    }
	    return this.queue_cache;
	  }

	  public synchronized void changeAcceptQueue()
	  {
	    if (this.isGetting) {
	      return;
	    }
	    this.using_queue_cache = (!this.using_queue_cache);
	    this.using_queue_cache_sec = (!this.using_queue_cache_sec);
	  }
}
