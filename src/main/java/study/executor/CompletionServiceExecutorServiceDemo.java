package study.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
 
/**
 * java 并发
 * http://blog.csdn.net/lmj623565791/article/details/27250059
 * 
 * 带返回结果的批量任务执行
 * CompletionService
 * 
 * ExectorService.invokeAll
 *
 */
public class CompletionServiceExecutorServiceDemo {
	
	public static void main(String[] args) throws Exception {
		
		doCompletionService();
		
//		doExecutorServiceInvokeAll();
		
	}
	
	/** 演示CompletionService 
	 * @throws ExecutionException 
	 * @throws InterruptedException
	 * 
	 * CompletionService将Executor（线程池）和BlockingQueue（阻塞队列）结合
	 * 在一起，同时使用Callable作为任务的基本单元，整个过程就是生产者不断把Callable
	 * 任务放入阻塞对了，Executor作为消费者不断把任务取出来执行，并返回结果；
	 *  */
	public static void doCompletionService() throws InterruptedException, ExecutionException{
		// 初始化定义
		ExecutorService executor = Executors.newFixedThreadPool(10);
		// 容量为10的阻塞队列 
		BlockingQueue<Future<Integer>> completionQueue  = new LinkedBlockingDeque<Future<Integer>>(10);
		CompletionService<Integer> service = new ExecutorCompletionService<Integer>(executor, completionQueue);
		
		/* 开始执行任务
		 * 模拟产生了10个任务
		 */
		for(int i = 0 ; i < 20 ; i ++){
			Future<Integer> submit = service.submit(new Callable<Integer>() { //submit 返回的引用,可以用来获取对应的返回的值 .
				// Callable 相当于 Runnable
				public Integer call() throws Exception { 
					//任务在这里面执行
					int sleep = new Random().nextInt(10000);
					Thread.sleep(sleep);
					System.out.println(Thread.currentThread().getName() + " 休息了" + sleep + "毫秒");
					return sleep;
				}
			});
			//System.out.println("submit:" + submit.get());
		}
		
		//读取返回的结果 , 谁有结果返回, 就是返回谁的结果 , 在这里可以做一个无限的循环作为一个遍历
		for(int i = 0 ; i < 10 ; i ++){
			Future<Integer> take = service.take();
			System.out.println("take:" + take.get());//future.get(timeout, unit);可以设置超时的世家
		}
		
		//情况线程池
		executor.shutdown();
	}
	
	/** 演示ExecutorService 
	 * 
	 * ExecutorService的invokeAll方法也能批量执行任务，并批量返回结果，但是呢，
	 * 有个我觉得很致命的缺点，必须等待所有的任务执行完成后统一返回，一方面内存持有的
	 * 时间长；另一方面响应性也有一定的影响，毕竟大家都喜欢看看刷刷的执行结果输出，而不是苦苦的等待；
	 * @throws InterruptedException 
	 * @throws ExecutionException 
	 * @throws TimeoutException 
	 * */
	public static void doExecutorServiceInvokeAll() throws InterruptedException, ExecutionException, TimeoutException{
		ExecutorService mThreadPool = Executors.newFixedThreadPool(10);
		List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
		
		for(int i = 0 ; i < 20 ; i++){
			Callable<Integer> callable = new Callable<Integer>() {
				public Integer call() throws Exception {
					int sleep = new Random().nextInt(2000);
					Thread.sleep(sleep);
					System.out.println(Thread.currentThread().getName() + "休息了:" + sleep);
					return sleep;
				}
			};
			
			tasks.add(callable);
		}
 
		long startTime = System.currentTimeMillis();
		// 执行并返回了结果
		List<Future<Integer>> invokeAll = mThreadPool.invokeAll(tasks);
		long allTime = System.currentTimeMillis() - startTime;
		System.out.println("执行了:" + allTime);
		
		//将所有的结果答应出来
		for(Future<Integer> future : invokeAll){
			System.out.println(future.get()); //future.get(timeout, unit);在该方式下似乎并没有用
		}
		
		mThreadPool.shutdown();
	}
}

