package study.schedule.scheduledexecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 注意：ScheduledExecutorService 中两种最常用的调度方法 ScheduleAtFixedRate 和
 * ScheduleWithFixedDelay。ScheduleAtFixedRate 每次执行时间为上一次任务开始起向后推一个时间间隔，即每次执行时间为
 * :initialDelay, initialDelay+period, initialDelay+2*period,
 * …；ScheduleWithFixedDelay 每次执行时间为上一次任务结束起向后推一个时间间隔，即每次执行时间为：initialDelay,
 * initialDelay+executeTime+delay,
 * initialDelay+2*executeTime+2*delay。由此可见，ScheduleAtFixedRate
 * 是基于固定时间间隔进行任务调度，ScheduleWithFixedDelay 取决于每次任务执行的时间长短，是基于不固定时间间隔进行任务调度。
 * 
 * @author zhouyelin
 *
 */
public class ScheduledExecutorTest implements Runnable {

	private String jobName = "";

	public ScheduledExecutorTest(String jobName) {
		super();
		this.jobName = jobName;
	}

	@Override
	public void run() {
		System.out.println("execute " + jobName);
	}

	public static void main(String[] args) {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
		long initialDelay1 = 1;
		long period1 = 1;
		// 从现在开始1秒钟之后，每隔1秒钟执行一次job1
		service.scheduleAtFixedRate(new ScheduledExecutorTest("job1"), initialDelay1, period1, TimeUnit.SECONDS);
		long initialDelay2 = 1;
		long delay2 = 1;
		// 从现在开始2秒钟之后，每隔2秒钟执行一次job2
		service.scheduleWithFixedDelay(new ScheduledExecutorTest("job2"), initialDelay2, delay2, TimeUnit.SECONDS);
	}

}
