package com.dodo.eurekaribbon.simulate;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * 模拟信号量隔离
 */
public class HystrixSemaphoreIsolation {
    public static Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();


    //限流
    @Test
    public void seriveA() throws InterruptedException {
        String serviceId = "seriveA";
        Semaphore semaphore = getSemaphore(serviceId);
        for (int i = 0; i <= 100; i++) {
            //用来模拟并发 ,Tomcat 线程并发
            new Thread(() -> {
                boolean flag = semaphore.tryAcquire();
                //
                if (flag) {
                    System.out.println("业务处理A");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    semaphore.release(1);
                } else {
                    System.out.println("资源限制,保护资源");
                }
            }).start();
        }
    }


    /**
     * 隔离
     * 假设 A 服务发生堵塞，B服务正常，在使用隔离后，访问 B 服务依然正常。
     */
    @Test
    public void seriveAB() {
        String serviceIdA = "seriveA";
        Semaphore semaphoreA = getSemaphore(serviceIdA);
        for (int i = 0; i <= 100; i++) {
            //用来模拟并发 ,Tomcat 线程并发
            new Thread(() -> {
                //获取资源
                boolean flag = semaphoreA.tryAcquire();
                //
                if (flag) {
                    System.out.println("业务处理A");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //释放资源
                    semaphoreA.release(1);
                } else {
                    System.out.println("资源限制A,保护资源");
                }
            }).start();
        }
        //
        String serviceIdB = "seriveB";
        Semaphore semaphoreB = getSemaphore(serviceIdB);
        for (int i = 0; i <= 100; i++) {
            //用来模拟并发 ,Tomcat 线程并发
            new Thread(() -> {
                //获取资源
                boolean flag = semaphoreB.tryAcquire();
                //
                if (flag) {
                    System.out.println("业务处理B");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //释放资源
                    semaphoreB.release(1);
                } else {
                    System.out.println("资源限制B,保护资源");
                }
            }).start();
        }

    }


    /**
     * 获取 Semaphore
     *
     * @param serviceId
     * @return
     */
    public static Semaphore getSemaphore(String serviceId) {
        Semaphore semaphore = semaphoreMap.get(serviceId);
        if (semaphore == null) {
            semaphore = new Semaphore(5);
            semaphoreMap.put(serviceId, semaphore);
        }
        return semaphore;
    }

    /**
     * Hystrix信号量创建分析
     *
     * TryableSemaphoreActual implements TryableSemaphore
     * ->
     * tryAcquire() release() getNumberOfPermitsUsed()
     *
     */
}
