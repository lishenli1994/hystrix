package com.dodo.eurekafeign.simulate;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 模拟多线程池隔离
 */
public class HystrixThreadIsolation {

    public static Map<String, ExecutorService> executorServiceMap = new ConcurrentHashMap<>();

    /**
     * 限流
     * 单个任务执行，当任务数量超过限制，会拒绝请求，保护服务。
     * 这就是 Hystrix 的资源限制保护
     *
     * @throws InterruptedException
     */
    @Test
    public void serviceA()  throws InterruptedException {
        String serviceId = "A";
        ExecutorService executorService = getExecutors(serviceId);
        for (int i = 0; i <= 100; i++) {
            int tmp = i;
            executorService.execute(() -> {
                System.out.println("执行任务: " + tmp);
            });
        }
        Thread.sleep(1000000);
    }

    /**
     * 隔离
     * 假设服务 A 发生堵塞，服务 B正常。
     * 如果 A 和 B 用的是 同一个线程池，那么 AB 肯定都堵塞。
     * 如果 A 和 B 都是用自己的 线程池 ，即使 A 堵塞了，B 还能正常。
     * 这就是 隔离的 Hystrix 原理
     * @throws InterruptedException
     */
    @Test
    public void TestServiceAB() throws InterruptedException {
        //假设 服务A 调用发送堵塞
        String serviceIdA = "A";
        ExecutorService executorServiceA = getExecutors(serviceIdA);
        for (int i = 0; i <= 100; i++) {
            int tmp = i;
            executorServiceA.execute(() -> {
                //模拟堵塞
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("执行任务A: " + tmp);
            });
        }

        String serviceIdB = "B";
        ExecutorService executorServiceB = getExecutors(serviceIdB);
        for (int i = 0; i <= 100; i++) {
            int tmp = i;
            executorServiceB.execute(() -> {
                System.out.println("执行任务B: " + tmp);
            });
        }
        Thread.sleep(1000000);
    }


    /**
     * 为每个服务创建一个固定长度的线程池
     *
     * @param serviceId
     * @return
     */
    public static ExecutorService getExecutors(String serviceId) {
        ExecutorService executorService = null;
        if (!executorServiceMap.containsKey(serviceId)) {
            executorService = new ThreadPoolExecutor(5, 5,
                    10000, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(5), Executors.defaultThreadFactory(),(r,  executor) -> {
                System.out.println(serviceId + " : 资源限制,保护资源");
            });
        } else {
            executorService = executorServiceMap.get(executorService);
        }
        return executorService;
    }

    /**
     * Hystrix线程池创建分析
     *
     * HystrixCommand -> AbstractCommand ->
     * initThreadPool(HystrixThreadPool fromConstructor, HystrixThreadPoolKey threadPoolKey,
     * com.netflix.hystrix.HystrixThreadPoolProperties.Setter threadPoolPropertiesDefaults)
     * ->
     * com.netflix.hystrix.HystrixThreadPool.Factory.getInstance(threadPoolKey,
     * threadPoolPropertiesDefaults) : fromConstructor;
     */

}
