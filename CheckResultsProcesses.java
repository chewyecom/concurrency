package conc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

public class CheckResultsProcesses {
	// managing concurrent processes
	
	public static void main(String[] args) {
		CheckResultsProcesses crs = new CheckResultsProcesses();
//		crs.task1();
//		crs.task2();
//		crs.task3();
		crs.task4();
	}
	private void task4() {
		p("\n**task4** fork/join framework + RecursiveTask");
		Double[] weights = new Double[10];
		ForkJoinTask<?> task = new WeighAnimalTask(weights, 0, weights.length);
		ForkJoinPool pool = new ForkJoinPool();
		Double sum = (Double)pool.invoke(task);
		
		p("");
		p("Sum: "+sum);
	}
	
	private void task3() {
		/**
		 * fork/join framework requires
		 * 1. create a ForkJoinTask
		 * 2. create the ForkJoinPool
		 * 3. Start the ForkJoinTask
		 */
		p("\n**task3** fork/join framework + RecursiveAction");
		Double[] weights = new Double[10];
		ForkJoinTask<?> task = new WeighAnimalAction(weights, 0, weights.length);
		ForkJoinPool pool = new ForkJoinPool();
		pool.invoke(task);
		
		p("");
		p("Weights: ");
		Arrays.asList(weights).stream().forEach(d-> p(d.intValue() + " "));
	}
	
	private void task2() {
		p("\n**task2** CyclicBarrier");
		ExecutorService srv = null;
		try {
			srv = Executors.newFixedThreadPool(4);
			LionPenManager mgr = new LionPenManager();
			CyclicBarrier c1 = new CyclicBarrier(4);
			CyclicBarrier c2 = new CyclicBarrier(4, () -> p(".... Pen Cleaned!") );
			for (int i=0; i<4; i++)
				srv.submit(()-> mgr.performTaskCyclically(c1,c2));
		}
		finally {
			if (srv != null) srv.shutdown();
		}
	}
	
	private void task1() {
		p("\n**task1** output is random");
		ExecutorService srv = null;
		try {
			srv = Executors.newFixedThreadPool(4);
			LionPenManager mgr = new LionPenManager();
			for (int i=0; i<4; i++)
				srv.submit(()-> mgr.performTask());
		}
		finally {
			if (srv != null) srv.shutdown();
		}
	}
	
	public class LionPenManager {
		private void removeAnimals() {
			p(". Removing animals");
		}
		private void cleanPen() {
			p(".. Cleaning the pen");
		}
		private void addAnimals() {
			p("... Adding animals");
		}
		private void performTask() {
			this.removeAnimals();
			this.cleanPen();
			this.addAnimals();
		}
		private void performTaskCyclically(CyclicBarrier c1, CyclicBarrier c2) {
			try {
				this.removeAnimals();
				c1.await();
				this.cleanPen();
				c2.await();
				this.addAnimals();
			}
			catch (InterruptedException | BrokenBarrierException e) {
				p("performTaskCyclically Catch InterruptedException | BrokenBarrierException ");
			}
		}
	}
	
	public class WeighAnimalTask extends RecursiveTask {
		private int start;
		private int end;
		private Double[] weights;
		public WeighAnimalTask(Double[] weights, int start, int end) {
			this.start = start;
			this.end = end;
			this.weights = weights;
		}
		protected Double compute() {
			if (end-start <=3) {
				double sum=0;
				for (int i=start; i<end; i++) {
					weights[i] = (double) new Random().nextInt(100);
					p("Animal Weighed (from random): "+"weights["+i+"] : " + weights[i]);
					sum += weights[i];
				}
				return sum;
			} else {
				int middle = start+((end-start)/2);
				p("[start="+start+",middle"+middle+",end="+end+"]");
				// multi-thread version
				RecursiveTask<Double> otherTask = new WeighAnimalTask(weights, start, middle);
				otherTask.fork();
				return new WeighAnimalTask(weights, middle, end).compute() + otherTask.join();
				
				/**
				 * Single-thread version
				 * RecursiveTask<Double> otherTask = new WeighAnimalTask(weights, start, middle);
				 * Double otherResult = otherTask.fork().join();
				 * return new WeighAnimalTask(weights, middle, end).compute() + otherResult;				 
				 */
			} 
		}
	}
	
	public class WeighAnimalAction extends RecursiveAction {
		private int start;
		private int end;
		private Double[] weights;
		public WeighAnimalAction(Double[] weights, int start, int end) {
			this.start = start;
			this.end = end;
			this.weights = weights;
		}
		protected void compute() {
			if (end-start <=3)
				for (int i=start; i<end; i++) {
					weights[i] = (double) new Random().nextInt(100);
					p("Animal Weighed (from random): "+"weights["+i+"] : " + weights[i]);
				}
			else {
				int middle = start+((end-start)/2);
				p("[start="+start+",middle"+middle+",end="+end+"]");
				invokeAll(new WeighAnimalAction(weights, start, middle), 
						new WeighAnimalAction(weights, middle, end) );
			}
		}
	}
	
	private void p(String me) {
		System.out.println(LocalDateTime.now() +"     " + me);
	} 

}
