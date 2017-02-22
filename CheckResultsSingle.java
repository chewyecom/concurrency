package conc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CheckResultsSingle {
	private static int counter =0;
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		CheckResultsSingle cr = new CheckResultsSingle();
		cr.task5();
		
	}

	private void task1() throws InterruptedException {
		new Thread( () -> {
			for(int i=0; i<500; i++) CheckResultsSingle.counter++;
		}).start();

		while(CheckResultsSingle.counter < 100) {
			System.out.println("NOT reach yet");
			Thread.sleep(1000); // 1 sec
		}
		System.out.println("Reached!! "+ CheckResultsSingle.counter + " : " + counter);
	}

	public void task2() {
		ExecutorService service = null;
		try {
			service = Executors.newSingleThreadExecutor();
			System.out.println("begin");
			service.execute(()-> System.out.println("Task3 is running " ));
			service.execute(()-> {for (int i=0; i<3; i++)
				System.out.println("i "+ i);}
			);
			service.execute(()-> System.out.println("Task3 is running " ));
			System.out.println("end");
		} finally {
			if(service !=null) service.shutdown();
		}
	}
	private void task3() throws InterruptedException, ExecutionException {
		ExecutorService service = null;
		try {
			service = Executors.newSingleThreadExecutor();
			Future<?> result = service.submit(() -> {
				for(int i=0; i<500; i++) CheckResultsSingle.counter++;
			});
			result.get(10, TimeUnit.SECONDS);
			System.out.println("REACHED!!");
		} catch (TimeoutException e) {
			System.out.println("NOT reached in time");
		} finally {
			if (service!=null) service.shutdown();
		}
	}
	private void task4() throws InterruptedException, ExecutionException {
		ExecutorService service = null;
		try {
			service = Executors.newSingleThreadExecutor();
			Future<Integer> result = service.submit(() -> 
				30+11
			);
			System.out.println(result.get());
		} finally {
			if (service!=null) service.shutdown();
		}
	}
	private void task5() throws InterruptedException, ExecutionException {
		ExecutorService service = null;
		try {
			service = Executors.newSingleThreadExecutor();
			Future<Integer> result = service.submit(() -> 
				30+11
			);
			System.out.println(result.get());
		} finally {
			if (service!=null) service.shutdown();
		}
		if (service!=null) {
			service.awaitTermination(1,TimeUnit.MINUTES);
			if (service.isTerminated())
				System.out.println("All tasks finished");
			else
				System.out.println("At least one task is still running");
		}
	}
}
