package com.nowcoder.community;

import com.nowcoder.community.service.AlphaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ThreadPoolTests {


    //log输出内容的时候会带上线程id
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTests.class);

    // JDK普通线程池
    //会反复复用这5个线程
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    // JDK可执行定时任务的线程池
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    // Spring框架已经帮你初始化好，放到了容器中了，Spring普通线程池
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    // Spring可执行定时任务的线程池
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;
//
    @Autowired
    private AlphaService alphaService;

    private void sleep(long m) {
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 1.JDK普通线程池
    @Test
    public void testExecutorService() {
       //线程池需要分配一个任务，需要提供一个任务，一般实现Runable接口
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello : ExecutorService");
            }
        };
        for (int i = 0; i < 10; i++) {
            //调用这个submit方法之后，线程池就会分配一个线程执行这个线程体
            executorService.submit(task);
        }
        //需要线程sleep一下，不然还没执行for循环中的代码就结束了
        sleep(10000);
        //运行结果
       /* 2020-02-09 13:02:09,239 DEBUG [pool-1-thread-2] c.n.c.ThreadPoolTests [ThreadPoolTests.java:60] Hello : ExecutorService
        2020-02-09 13:02:09,239 DEBUG [pool-1-thread-1] c.n.c.ThreadPoolTests [ThreadPoolTests.java:60] Hello : ExecutorService
        2020-02-09 13:02:09,239 DEBUG [pool-1-thread-3] c.n.c.ThreadPoolTests [ThreadPoolTests.java:60] Hello : ExecutorService
        2020-02-09 13:02:09,239 DEBUG [pool-1-thread-4] c.n.c.ThreadPoolTests [ThreadPoolTests.java:60] Hello : ExecutorService
        2020-02-09 13:02:09,239 DEBUG [pool-1-thread-5] c.n.c.ThreadPoolTests [ThreadPoolTests.java:60] Hello : ExecutorService
        2020-02-09 13:02:09,239 DEBUG [pool-1-thread-2] c.n.c.ThreadPoolTests [ThreadPoolTests.java:60] Hello : ExecutorService
        2020-02-09 13:02:09,239 DEBUG [pool-1-thread-4] c.n.c.ThreadPoolTests [ThreadPoolTests.java:60] Hello : ExecutorService
        2020-02-09 13:02:09,239 DEBUG [pool-1-thread-3] c.n.c.ThreadPoolTests [ThreadPoolTests.java:60] Hello : ExecutorService
        2020-02-09 13:02:09,239 DEBUG [pool-1-thread-1] c.n.c.ThreadPoolTests [ThreadPoolTests.java:60] Hello : ExecutorService
        2020-02-09 13:02:09,239 DEBUG [pool-1-thread-5] c.n.c.ThreadPoolTests [ThreadPoolTests.java:60] Hello : ExecutorService*/

    }



    // 2.JDK定时任务线程池
    @Test
    public void testScheduledExecutorService() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello ScheduledExecutorService");
            }
        };
        scheduledExecutorService.scheduleAtFixedRate(task, 10000, 1000, TimeUnit.MILLISECONDS);

        sleep(30000);
    }

    // 3.Spring普通线程池
    @Test
    public void testThreadPoolTaskExecutor() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello ThreadPoolTaskExecutor");
            }
        };

        for (int i = 0; i < 10; i++) {
            taskExecutor.submit(task);
        }

        sleep(10000);

        //执行结果

       /* 2020-02-09 13:22:10,705 DEBUG [task-2] c.n.c.ThreadPoolTests [ThreadPoolTests.java:105] Hello ThreadPoolTaskExecutor
        2020-02-09 13:22:10,705 DEBUG [task-1] c.n.c.ThreadPoolTests [ThreadPoolTests.java:105] Hello ThreadPoolTaskExecutor
        2020-02-09 13:22:10,705 DEBUG [task-3] c.n.c.ThreadPoolTests [ThreadPoolTests.java:105] Hello ThreadPoolTaskExecutor
        2020-02-09 13:22:10,705 DEBUG [task-4] c.n.c.ThreadPoolTests [ThreadPoolTests.java:105] Hello ThreadPoolTaskExecutor
        2020-02-09 13:22:10,705 DEBUG [task-4] c.n.c.ThreadPoolTests [ThreadPoolTests.java:105] Hello ThreadPoolTaskExecutor
        2020-02-09 13:22:10,705 DEBUG [task-5] c.n.c.ThreadPoolTests [ThreadPoolTests.java:105] Hello ThreadPoolTaskExecutor
        2020-02-09 13:22:10,705 DEBUG [task-2] c.n.c.ThreadPoolTests [ThreadPoolTests.java:105] Hello ThreadPoolTaskExecutor
        2020-02-09 13:22:10,705 DEBUG [task-1] c.n.c.ThreadPoolTests [ThreadPoolTests.java:105] Hello ThreadPoolTaskExecutor
        2020-02-09 13:22:10,705 DEBUG [task-3] c.n.c.ThreadPoolTests [ThreadPoolTests.java:105] Hello ThreadPoolTaskExecutor
        2020-02-09 13:22:10,705 DEBUG [task-4] c.n.c.ThreadPoolTests [ThreadPoolTests.java:105] Hello ThreadPoolTaskExecutor*/
    }
//
    // 4.Spring定时任务线程池
    @Test
    public void testThreadPoolTaskScheduler() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello ThreadPoolTaskScheduler");
            }
        };

        //当前时间延迟10000毫秒
        Date startTime = new Date(System.currentTimeMillis() + 10000);
        taskScheduler.scheduleAtFixedRate(task, startTime, 1000);

        sleep(30000);
    }

    // 5.Spring普通线程池(简化)，普通方法也可以做线程体
    @Test
    public void testThreadPoolTaskExecutorSimple() {
        for (int i = 0; i < 10; i++) {
            alphaService.execute1();
        }


        //必须阻塞一下，不然主方法执行完了，for循环中方法还没结束
        sleep(10000);
    }

    // 6.Spring定时任务线程池(简化)
    @Test
    public void testThreadPoolTaskSchedulerSimple() {
        sleep(30000);
    }

}
