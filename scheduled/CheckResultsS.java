package scheduled;

import java.awt.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CheckResultsS {
	private static int counter =0;

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		CheckResultsS cr = new CheckResultsS();
		cr.tasks1();

	}
	
	private void tasks1() throws InterruptedException, ExecutionException {
		ScheduledExecutorService service = null;
		try {
			service = Executors.newSingleThreadScheduledExecutor();
			
			Runnable task1 = () -> {System.out.println( getLog() + " : task1 is running"); task1(); };
			Callable<String> task2 = () -> {task1(); return getLog() + " : task2 is running ";};			
			
			Future<?> result1 = service.schedule(task1, 10, TimeUnit.SECONDS);
			Future<?> result2 = service.schedule(task2, 1, TimeUnit.MINUTES);
			
			System.out.println(getLog() );
			System.out.println(result1.get());
			System.out.println(getLog() );
			System.out.println(result2.get());
			
			// The following example executes a Runnable task every minute,
			// following an initial five-minutes delay;
			short initialDelay=1, period=1;
			Runnable task3 = () -> {task1(); System.out.println(getLog() + " : task3 is running");};
			Future<?> result3 = service.schedule(task3, 1, TimeUnit.MINUTES);
			service.scheduleAtFixedRate(task3, initialDelay, period, TimeUnit.MINUTES);
			System.out.println(LocalDateTime.now());
			System.out.println(result3.get());
		} finally {
			if (service!=null) {
				System.out.println(getLog()  + " : Service is not null");
				Thread.sleep(120000); // 120sec=2min
				if (service!=null) {
					System.out.println(getLog()  + " : 2nd Service is not null");
					service.shutdown();
				}				
			}
		}

	}
	
	private String getLog() {
		return LocalDateTime.now() + " : " + Runtime.getRuntime().freeMemory();
	}
	
	private void task1() {
		// create a large array to consume memory
		ArrayList l = new ArrayList();
		for(int i=0; i<100000; i++) {
			l.add(i);
		}
	}

}
