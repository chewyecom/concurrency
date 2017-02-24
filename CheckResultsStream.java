package conc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CheckResultsStream {

	public static void main(String[] args) {
		CheckResultsStream crs = new CheckResultsStream();
		crs.task1();
		//crs.task2(); this take approx 48 sec
		crs.taskAvoid1();
		crs.task3();
		crs.task4();
		crs.task5();
		crs.task6();
		
	}
	private void task6() {
		System.out.println("\n"+LocalDateTime.now());
		// parallel stream
		Stream<String> zoo = Stream.of("lions", "tigers", "bears").parallel();
		ConcurrentMap<Integer, String> zooMap = zoo.collect(
				Collectors.toConcurrentMap(String:: length , k-> k, (s1,s2) -> s1 + "," + s2));
		System.out.println(zooMap);
		System.out.println(zooMap.getClass());
		System.out.println("\n"+LocalDateTime.now());
		
		// parallel reduction
		// to have JVM to take advantage of the parallel structures
		// using groupingByConcurrent collector on a parallel stream > groupingBy collector
		Stream<String> zoo2 = Stream.of("lions", "tigers", "bears").parallel();
		ConcurrentMap<Integer, List<String>> zooMap2 = zoo2.collect(
				Collectors.groupingByConcurrent(String:: length));
		System.out.println(zooMap2);
		System.out.println(zooMap2.getClass());
		System.out.println("\n"+LocalDateTime.now());
	}
	
	private void task5() {
		// Combing Result with collect()
		System.out.println("\n\n**task5**ConcurrentSkipListSet**");
		Stream<String> stream = Stream.of("w","o","l","f").parallel();
		SortedSet<String> set = stream.collect(ConcurrentSkipListSet:: new, Set::add, Set::addAll);
		System.out.println("\n"+set);
		System.out.println("\n"+LocalDateTime.now());
		
		// Using One-Argument collect() Method
		Stream<String> stream2 = Stream.of("w","o","l","f").parallel();
		Set<String> set2 = stream2.collect(Collectors.toSet());
		System.out.println("\n"+set2);
		System.out.println("\n"+LocalDateTime.now());
	}
	
	private void task4() {
		// stream().reduce() is typically to return single result by reduction of a list of objects. 
		System.out.println("\n\n**task4**reduce()**");
		System.out.println("\n"+LocalDateTime.now() + " : forEachOrdered ");
		System.out.println(Arrays.asList('w','o','l','f','a','p','p','l','e').stream()
				.reduce("", (c,s1) -> c+s1, (s2,s3) -> s2+s3));
		
		System.out.println("\n"+LocalDateTime.now() + " : parallelStream ");
		System.out.println(Arrays.asList(1,2,3,4,5,6).parallelStream()
				.reduce(0, (a,b) -> (a-b))); // NOT IN ASSOCIATIVE ACCUMULATOR 

		System.out.println("\n"+LocalDateTime.now() + " : parallelStream ");
		System.out.println(Arrays.asList("w","o","l","f").parallelStream()
				.reduce("X", String::concat));  
		System.out.println("\n"+LocalDateTime.now());

	}
	
	private void task3() {
		System.out.println("\n\n**task3**unordered**");
		System.out.println("\n"+LocalDateTime.now() + " : forEachOrdered ");
		Arrays.asList(1,2,3,4,5,6).stream().forEachOrdered( i-> System.out.print(i+" "));
		System.out.println("\n"+LocalDateTime.now() + " : unordered().forEach() ");
		Arrays.asList(1,2,3,4,5,6).stream().unordered().forEach( i-> System.out.print(i+" "));
		System.out.println("\n"+LocalDateTime.now()+ " : unordered().parallel.forEach() ");
		Arrays.asList(1,2,3,4,5,6).stream().unordered().parallel().forEach( i-> System.out.print(i+" "));
		System.out.println("\n"+LocalDateTime.now());
		
	}
	
	private void task2() {
		Cal cal = new Cal();
		
		// Define the data
		List<Integer> data = new ArrayList<Integer>();
		for(int i=0; i<4000; i++) data.add(i);
		
		// Process the data
		long start = System.currentTimeMillis();
		cal.processAll(data);
		double time = (System.currentTimeMillis()-start)/1000.0;
		
		// Report results
		System.out.println("\n Stream Task2 completed in : "+time+" seconds");
	}
	
	private void task1() {
		// parallel
		Stream<Integer> s = Arrays.asList(1,2,3,4,5,6).stream();
		Stream<Integer> ps = s.parallel();
		
		// parallelStream()
		Stream<Integer> ps2 = Arrays.asList(1,2,3,4,5,6).parallelStream();
		
		System.out.println("**task1**");
		System.out.println("stream : ");
		Arrays.asList(1,2,3,4,5,6)
		.stream()
		.forEach( ea -> System.out.print(ea + " " ) );
		
		System.out.println("\n parallelStream : ");
		Arrays.asList(1,2,3,4,5,6)
		.parallelStream()
		.forEach( ea -> System.out.print(ea + " " ) );
		
		// forces a parallel stream to process the results in order at the cost of performance
		System.out.println("\n forEachOrdered : ");
		Arrays.asList(1,2,3,4,5,6)
		.parallelStream()
		.forEachOrdered( ea -> System.out.print(ea + " " ) );
		
	}

	public class Cal {
		public int process(int input) {
			try{
				Thread.sleep(10);
			} catch (InterruptedException ie) {
				System.out.println("Cal process catch InterruptedException");
			}
			return input++;
		}
		public void processAll(List<Integer> data) {
			data.stream().map(a -> process(a)).count(); 
		}
	}
	
	private void taskAvoid1() {
		// Stateful lambda expression should be avoided
		
		/** use SynchronizedList
		 * For an ArrayList object, the JVM internally manages a primitive array of the same type.
		 * As ArrayList grows, new and larger primitive array is required.
		 * If two threads both trigger the array to be resized at the same time, a result can be lost,
		 * producing the unexpected value shown
		 *  
		 */
		List<Integer> syncdata = Collections.synchronizedList(new ArrayList<>());
		System.out.println("\n\n**taskAvoid1**");
	
		System.out.println("\n**NOT Synchronized Array**");
		List<Integer> data = new ArrayList<>();
		Arrays.asList(1,2,3,4,5,6).parallelStream()
			.map(i-> {data.add(i); return i;} ) // AVOID STATEFUL LAMBDA EXPRESION!!
			.forEachOrdered( i-> System.out.print(i+" "));
		System.out.println();
		for(Integer e: data) {
			System.out.print(e+" ");
		}
		
		System.out.println("\n**Synchronized Array**");
		Arrays.asList(1,2,3,4,5,6).parallelStream()
			.map(i-> {syncdata.add(i); return i;} ) // AVOID STATEFUL LAMBDA EXPRESION!!
			.forEachOrdered( i-> System.out.print(i+" "));
		System.out.println();
		for(Integer e: syncdata) {
			System.out.print(e+" ");
		}

	}
}
