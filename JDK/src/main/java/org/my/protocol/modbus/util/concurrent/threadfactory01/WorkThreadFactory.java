package org.my.protocol.modbus.util.concurrent.threadfactory01;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkThreadFactory implements ThreadFactory {
	
	private AtomicInteger atomicInteger = new AtomicInteger(0);
	
	@Override
	public Thread newThread(Runnable r)
	{
		int c = atomicInteger.incrementAndGet();  
                System.out.println("create no " + c + " Threads");  
		return new WorkThread(r, atomicInteger);//通过计数器，可以更好的管理线程
	}
 
}