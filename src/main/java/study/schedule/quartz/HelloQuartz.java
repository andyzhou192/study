package study.schedule.quartz;

import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloQuartz implements Job {

	@Override
	//@DisallowConcurrentExecution //防止任务并发执行。
	//注意，@DisallowConcurrentExecution是对JobDetail实例生效，也就是如果你定义两个JobDetail，引用同一个Job类，是可以并发执行的
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail detail = context.getJobDetail();
		String name = detail.getJobDataMap().getString("name");
		System.out.println("say hello to " + name + " at " + new Date());
		
	}

}
