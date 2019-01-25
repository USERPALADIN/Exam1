package com.example.firstTask.thread;

import com.example.firstTask.controller.MainController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class UtilFindMaxValue implements Runnable {
	
	
	private List<String> list;
	private int minSize;
	private int maxSize;
	private int min;
	private int max;
	
	@Override
	public void run() {
		long startTime  = System.currentTimeMillis();
		for (int i = minSize; i < maxSize; i++) {
			max = Integer.parseInt(list.get(i));
			if (min < max) {
				min = max;
			}
//			System.out.println(Thread.currentThread().getName() + " + " + i);
//			System.out.println(min);
			Thread.yield();
		}
		MainController.results.add(min);
		long timeSpent = System.currentTimeMillis() - startTime;
	//	System.out.println(timeSpent);
		MainController.time += timeSpent;
	}
	
	public UtilFindMaxValue(int minSize, int maxSize, List<String> list) {
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.list = list;
	}
	
}
