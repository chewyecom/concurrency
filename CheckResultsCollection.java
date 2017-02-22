package conc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * ConcurrentHashMap (implements ConcurrentMap ...)
 * ConcurrentLinkedDeque (implements Deque ...)
 * ConcurrentLinkedQueue (implements Queue ...)
 * ConcurrentSkipListMap (implements interfaces ConcurrentMap, SortedMap, NavigableMap, NavigableSet ...) 
 * ConcurrentSkipListSet (implements interfaces SortedSet, NavigableSet ...)
 * CopyOnWriteArrayList (implements List ...)
 * LinkedBlockingDeque (implements BlockingQueue, BlockingDeque ...) // maintains a doubly linked list between elements
 * LinkedBlockingQueue (implements BlockingQueue ...)
 * 
 * The BlockingQueue is like a regular Queue, except that is includes 
 *      methods that will wait a specific amount of time to complete and operation.
 *      
 * CopyOnWrite classes can use a lot of memory
 */
/**
 * Collections interface synchronized methods
 * synchronizedCollection
 * synchronizedList
 * synchronizedMap
 * synchronizedNavigableMap
 * synchronizedNavigableSet
 * synchronizedSet
 * synchronizedSortedMap
 * synchronizedSortedSet
 *
 */

public class CheckResultsCollection {

	public static void main(String[] args) {
		CheckResultsCollection crc = new CheckResultsCollection();
		//crc.task1();
		//crc.task2();
		//crc.task3();
		//crc.task4();
		crc.task5();
	}
	
	private void task5() {
		// similar to task1, this code throw cme, even though it use the Collections.synchronizedMap 
		try {
			FoodData fd = new FoodData();
			fd.put("penguin", 1);
			fd.put("flamingo", 2);
			
			Map<String, Object> syncFD = Collections.synchronizedMap(fd.foodData);
			// The keyset is not properly updated after the first element is removed.
			// hence, throw the CME
			for(String key: syncFD.keySet())
				syncFD.remove(key);
		}
		catch (ConcurrentModificationException cme) {
			System.out.println("task5 catch a ConcurrentModificationException");
		}
	}
	
	private void task4() {
		List<Integer> list = Collections.synchronizedList(new ArrayList<>(Arrays.asList(4,3,52)));
		synchronized(list) {
			for(int data: list)
				System.out.print(data+ " ");
		}
		
	}
	
	private void task3() {
		System.out.print("1st use ArrayList : ");
		List<Integer> aList = new ArrayList<>(Arrays.asList(4,3,52));
		try {
			for(Integer item: aList) {
				System.out.print(item+" ");
				aList.add(9);
			}
		} catch (ConcurrentModificationException cme) {
			System.out.println("ArrayList Catch a ConcurrentModificationException");
		}
		
		System.out.print("2nd use CopyOnWriteAL : ");
		List<Integer> cowList = new CopyOnWriteArrayList<>(Arrays.asList(4,3,52));
		for(Integer item: cowList) {
			System.out.print(item+" ");
			cowList.add(9);
		}
		System.out.println();
		System.out.print("After adding three 9 : ");
		for(Integer item: cowList) {
			System.out.print(item+" ");
		}
		System.out.println(" ; Size: "+cowList.size());
	}
	
	private void task2() {
		Map<String, String> zooMap = new ConcurrentHashMap<String, String>();
		zooMap.put("zebra", "grass");
		zooMap.put("elephant", "banana");
		System.out.println(zooMap.get("elephant"));
		
		Queue<String> rainbowq = new ConcurrentLinkedQueue<>();
		rainbowq.offer("purple");
		rainbowq.offer("blue");
		rainbowq.offer("light green");
		rainbowq.offer("green");
		rainbowq.offer("yellow");
		rainbowq.offer("orange");
		rainbowq.offer("red");
		System.out.println(rainbowq.peek());  // retrieve first
		System.out.println(rainbowq.poll());  // retrieve and remove first
		System.out.println(rainbowq.poll());  // retrieve and remove first
		
		Deque<String> deque = new ConcurrentLinkedDeque<>();
		deque.offer("Do"); // insert
		deque.push("Re");   // insert
		deque.push("Me");   // insert
		deque.push("Fa");   // insert
		deque.push("So");   // insert
		System.out.println(deque.peek()); // retrieve first
		System.out.println(deque.pop()); // remove first 
		System.out.println(deque.peek()); // retrieve first
		System.out.println(deque.pop()); // remove first 
		System.out.println(deque.peek()); // retrieve first
		
		try {
			BlockingQueue<String> bq = new LinkedBlockingQueue<>();
			bq.offer("sweet");
			bq.offer("sour", 15, TimeUnit.SECONDS);
			bq.offer("bitter", 15, TimeUnit.SECONDS);
			bq.offer("spicy", 15, TimeUnit.SECONDS);
			System.out.println(bq.poll());
			System.out.println(bq.poll(20, TimeUnit.MILLISECONDS));
			
		} catch (InterruptedException ie) {
			System.out.println("BlockingQueue Catch a InterruptedException");
		}
		
		try {
			BlockingDeque<String> bd = new LinkedBlockingDeque<>();
			bd.offer("dot");
			bd.offerFirst("line", 2, TimeUnit.MINUTES);
			bd.offer("triangle", 15, TimeUnit.SECONDS);
			bd.offer("square", 15, TimeUnit.SECONDS);
			bd.offer("pentagon", 15, TimeUnit.SECONDS);
			bd.offerLast("hexagon", 15, TimeUnit.MICROSECONDS);
			System.out.println(bd.poll());
			System.out.println(bd.poll(950, TimeUnit.MILLISECONDS));
			System.out.println(bd.pollFirst(200, TimeUnit.NANOSECONDS));
			System.out.println(bd.pollLast(1, TimeUnit.SECONDS));
			
		} catch (InterruptedException ie) {
			System.out.println("BlockingDequeue Catch a InterruptedException");
		}
		
	}
	
	private void task1() {
		try {
			FoodData fd = new FoodData();
			fd.put("penguin", 1);
			fd.put("flamingo", 2);
			
			// The keyset is not properly updated after the first element is removed.
			// hence, throw the CME
			for(String key: fd.foodData.keySet())
				fd.foodData.remove(key);
		}
		catch (ConcurrentModificationException cme) {
			System.out.println("task1 catch a ConcurrentModificationException");
		}
	}
	
	public class FoodData {
		// version 1: synchronized keyword modifier
		private Map<String, Object> foodData = new HashMap<String, Object>();
		
		public synchronized void put(String key, int value) {
			foodData.put(key, value);
		}
		public synchronized Object set(String key) {
			return foodData.get(key);		
		}
	}
	
	public class ConcurrentFoodData {
		// version 2: concurrent collection class
		private ConcurrentHashMap<String, Object> foodDataCon = new ConcurrentHashMap<String, Object>();
		
		public void put(String key, String value) {
			foodDataCon.put(key, value);
		}
		public Object syncSet(String key) {
			return foodDataCon.get(key);		
		}
	}
	
}
