package study.schedule.jcrontab;

import org.jcrontab.Crontab;
import org.jcrontab.NativeExec;
import org.jcrontab.SendMail;
import org.jcrontab.data.CrontabParser;
import org.jcrontab.data.CrontabEntryBean;
import org.jcrontab.data.CrontabEntryDAO;
import java.io.File;

/**
 * https://blog.csdn.net/maskice/article/details/1670070
 * 将C:\work\workspace\study\src\main\resources\libs目录下的所有jar引入工程依赖包
 * @author zhouyelin
 *
 */
public class JCrontabApp {
    private static Crontab cron = null;
    private String JcrontabFile = null;

    public JCrontabApp() {
    }

    public String getJcrontabFile() {
        return JcrontabFile;
    }

    public void setJcrontabFile(String jcrontabFile) {
        JcrontabFile = jcrontabFile;
    }

    /**
     * 初始化Jcrontab，通过指定的jcrontab.properties来执行具体的操作
     * 启动Jcrontab
     */
    protected void init() {
        cron = Crontab.getInstance();
        try {
            ShutdownHook();
            cron.setDaemon(false);
            if (null == JcrontabFile)
                cron.init();
            cron.init(JcrontabFile);
            System.out.println("Start Jcrontab...");
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭Jcrontab的执行
     * @throws Exception
     */
    protected void ShutdownHook() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutting down...");
                cron.uninit(200);
                System.out.println("Stoped Jcrontab!");
            }
        });
    }

    /**
     * 判断是否是Holiday
     * @return
     * @throws Exception
     */
    public boolean isHoliday()throws Exception{
        return cron.isHoliday();
    }

    /**
     * 根据不同的参数执行不同的定时程序，可选参数可从main方法中查看
     * @param args
     * @throws Exception
     */
    public void exectue(String[] args) throws Exception {
        if (args.length < 2) {
            return;
        }
        this.setJcrontabFile(args[0]);
        init();
        if ("database".equals(args[1]))
            executeDatabase();
        else if ("appliction".equals(args[1])) {
            String[] parameters = new String[args.length-2];
            System.arraycopy(args,2,parameters,0,args.length-2);
            cron.newTask("org.jcrontab.NativeExec","main",parameters);
        } else if ("javamail".equals(args[1]))
            executeJavaMail(args[2]);
    }

    /**
     * 执行数据库的操作
     * @throws Exception
     */
    protected void executeDatabase() throws Exception {
        CrontabParser cp = new CrontabParser();
        CrontabEntryBean[] ceb = new CrontabEntryBean[2];
        ceb[0] = cp.marshall("* * * * * com.aweb.test.NumTest 123");
        ceb[0].setYears("*");
        ceb[0].setSeconds("0");
        ceb[0].setBusinessDays(true);
        ceb[0].setId(0);

        ceb[1] = cp.marshall("* * * * * com.aweb.test.LetterTest 234");
        ceb[1].setYears("*");
        ceb[1].setSeconds("0");
        ceb[1].setBusinessDays(true);
        ceb[1].setId(1);
        CrontabEntryDAO.getInstance().store(ceb);
    }

    /**
     * 执行本地的应用程序的操作
     * @param parameters
     */
    protected void executeAppliction(String[] parameters) {
        NativeExec.main(parameters);
    }

    /**
     * 将执行的文件发送为email
     * @param sendFilename
     * @throws Exception
     */
    protected void executeJavaMail(String sendFilename) throws Exception {
        File sendFile = new File(sendFilename);
        SendMail sm = new SendMail();
        sm.send(sendFile);
    }

    /**
     * 主要通过main方法来实现Jcrontab的实现
     * 在命令行输入jcrontab.properties文件的位置，和要处理的类型，这里，我们选filedata,接着运行该类
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
    	String[] defaultArgs = {"C:\\work\\workspace\\study\\src\\main\\java\\study\\schedule\\jcrontab\\jcrontab.properties", "-filedata"};
        if(args.length == 0) args = defaultArgs;
    	if (args.length < 2) {
            System.out.println("Usage:The values of args:<jcrontabfile> <-type> [<parameters>]");
            System.out.println("********************************************************");
            System.out.println("Optional Parameters of type:");
            System.out.println("-filedata:doing file operating.");
            System.out.println("-database:doing database operating.");
            System.out.println("-appliction <command>:doing native application execute.");
            System.out.println("-javamail <sendFile>:doing javamail operating.");
            System.out.println("********************************************************");
            System.exit(1);
        }
        JCrontabApp jp = new JCrontabApp();
        jp.exectue(args);
        System.out.println("isHoliday:" + jp.isHoliday());
    }
}
