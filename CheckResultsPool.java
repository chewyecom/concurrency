package conc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CheckResultsPool {
	private static int counter =0;
	private static AtomicInteger acounter= new AtomicInteger(0);

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		CheckResultsPool cr = new CheckResultsPool();
		//cr.tasks1();
		//cr.tasks2();
		//cr.tasks3();
		//cr.tasks4();
		cr.tasks5();

	}
	
	private void tasks1() throws InterruptedException, ExecutionException {
		ExecutorService service = null;
		try {
			service = Executors.newFixedThreadPool(20);
			for(int i=0; i<10; i++)
				service.submit(() -> {
					System.out.println("tasks1 " + CheckResultsPool.counter++);
					System.out.println("tasks1 : atomic : " + CheckResultsPool.acounter.getAndIncrement());
				});
		} finally {
			if(service!=null)
				service.shutdown();
		}
	}
	
	private void tasks2() throws InterruptedException, ExecutionException {
		ExecutorService service = null;
		try {
			service = Executors.newFixedThreadPool(20);
			for(int i=0; i<10; i++)
				service.submit(() -> task2());
		} finally {
			if(service!=null)
				service.shutdown();
		}
	}
	
	private void task2() {
		// synchronized block
		synchronized(this) {
			System.out.println("task2 : " + CheckResultsPool.counter++);
			System.out.println("taks2 : atomic : " + CheckResultsPool.acounter.getAndIncrement());
		}
	}
	
	// synchronized modifier
	private synchronized void incrementCounter() {
		System.out.println("method incrementCounter : " + CheckResultsPool.counter++);
	}
	
	// static synchronization e.g. an Object
	public static void incrementCounterStatic() {
		synchronized(CheckResultsPool.class) {
			System.out.println("incrementCounterStatic : " + CheckResultsPool.counter++);
		}
	}
	
	// 
	public static synchronized void incrementCounterSyncStatic() {
		System.out.println("incrementCounterSyncStatic : " + CheckResultsPool.counter++);
	}
	
	private void tasks3() throws InterruptedException, ExecutionException {
		ExecutorService service = null;
		try {
			service = Executors.newFixedThreadPool(20);
			for(int i=0; i<10; i++)
				service.submit(() -> incrementCounter());
		} finally {
			if(service!=null)
				service.shutdown();
		}
	}

	private void tasks4() throws InterruptedException, ExecutionException {
		ExecutorService service = null;
		try {
			service = Executors.newFixedThreadPool(20);
			for(int i=0; i<10; i++)
				service.submit(() -> CheckResultsPool.incrementCounterStatic());
		} finally {
			if(service!=null)
				service.shutdown();
		}
	}
	
	private void tasks5() throws InterruptedException, ExecutionException {
		ExecutorService service = null;
		try {
			service = Executors.newFixedThreadPool(20);
			for(int i=0; i<10; i++)
				service.submit(() -> CheckResultsPool.incrementCounterSyncStatic());
		} finally {
			if(service!=null)
				service.shutdown();
		}
	}
}
