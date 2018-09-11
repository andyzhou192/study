package study.schedule.quartz;

import static org.quartz.DateBuilder.newDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.GregorianCalendar;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.AnnualCalendar;

/**
 * Quartz 可以满足更多更复杂的调度需求
 * Quartz是一个任务调度框架。比如你遇到这样的问题:
 * 	1、想每月25号，信用卡自动还款
 * 	2、想每年4月1日自己给当年暗恋女神发一封匿名贺卡
 * 	3、想每隔1小时，备份一下自己的爱情动作片 学习笔记到云盘
 * 这些问题总结起来就是：在某一个有规律的时间点干某件事。
 * 并且时间的触发的条件可以非常复杂（比如每月最后一个工作日的17:50），
 * 复杂到需要一个专门的框架来干这个事。 Quartz就是来干这样的事，
 * 你给它一个触发条件的定义，它负责到了时间点，触发相应的Job起来干活。
 * 
 * Quartz最重要的3个基本要素：
 * 
 * 	Scheduler：调度器。所有的调度都是由它控制。
 * 	Trigger： 定义触发的条件。例子中，它的类型是SimpleTrigger，每隔1秒中执行一次（什么是SimpleTrigger下面会有详述）。
 * 	JobDetail & Job： JobDetail 定义的是任务数据，而真正的执行逻辑是在Job中，例子中是HelloQuartz。 
 * 	为什么设计成JobDetail + Job，不直接使用Job？这是因为任务是有可能并发执行，如果Scheduler直接使用Job，
 * 	就会存在对同一个Job实例并发访问的问题。而JobDetail & Job 方式，sheduler每次执行，都会根据JobDetail创建一个新的Job实例，
 * 	这样就可以规避并发访问的问题。
 * 
 * @author zhouyelin
 *
 */
public class QuartzTest {

	public static void main(String[] args) {
		try {
			//创建scheduler
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			
			/*
			 * 注意，所有的Calendar既可以是排除，也可以是包含，取决于：
			 * HolidayCalendar。指定特定的日期，比如20140613。精度到天。
			 * DailyCalendar。指定每天的时间段（rangeStartingTime, rangeEndingTime)，格式是HH:MM[:SS[:mmm]]。也就是最大精度可以到毫秒。
			 * WeeklyCalendar。指定每星期的星期几，可选值比如为java.util.Calendar.SUNDAY。精度是天。
			 * MonthlyCalendar。指定每月的几号。可选值为1-31。精度是天
			 * AnnualCalendar。 指定每年的哪一天。使用方式如上例。精度是天。
			 * CronCalendar。指定Cron表达式。精度取决于Cron表达式，也就是最大精度可以到秒。
			 */
			AnnualCalendar cal = new AnnualCalendar(); //定义一个每年执行Calendar，精度为天，即不能定义到2.25号下午2:00
			java.util.Calendar excludeDay = new GregorianCalendar();
			excludeDay.setTime(newDate().inMonthOnDay(2, 25).build());
			cal.setDayExcluded(excludeDay, true);  //设置排除2.25这个日期
			scheduler.addCalendar("FebCal", cal, false, false); //scheduler加入这个Calendar
			
			//定义一个Trigger（触发器）
			Trigger trigger= newTrigger().withIdentity("trigger1", "group1") //定义name/group
					.startNow() // 一旦加入Scheduler，立即生效
					.modifiedByCalendar("FebCal") //使用Calendar !!
					.withSchedule(simpleSchedule() //使用SimpleTrigger,指定从某一个时间开始，以一定的时间间隔（单位是毫秒）执行的任务
							.withIntervalInSeconds(1) //每隔一秒执行一次
							.repeatForever()) //一直执行，奔腾到老不停歇
					.build();
			
			//定义一个JobDetail
			JobDetail job = newJob(HelloQuartz.class) //定义Job类为HelloQuartz类，这是真正的执行逻辑所在
					.withIdentity("job1", "group1") //定义name/group
					.usingJobData("name", "quartz") //定义属性
					.build();
			
			//加入这个调度
			scheduler.scheduleJob(job, trigger);
			
			//启动之
			scheduler.start();
			
			//运行一段时间后关闭
			Thread.sleep(10000);
			scheduler.shutdown(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
