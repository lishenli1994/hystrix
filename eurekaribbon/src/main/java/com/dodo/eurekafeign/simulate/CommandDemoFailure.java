package com.dodo.eurekafeign.simulate;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * 线程池隔离
 */
public class CommandDemoFailure extends HystrixCommand<String> {

    private final String name;

    public CommandDemoFailure(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    public CommandDemoFailure(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool, String name) {
        super(group, threadPool);
        this.name = name;
    }

    public CommandDemoFailure(HystrixCommandGroupKey group, int executionIsolationThreadTimeoutMilliSecond, String name) {
        super(group, executionIsolationThreadTimeoutMilliSecond);
        this.name = name;
    }

    public CommandDemoFailure(Setter setter, String name) {
        super(setter);
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        System.out.println("this thread: " + Thread.currentThread().getName());
        return "hello demo";
    }

    @Override
    protected String getFallback() {
        try {
            Thread.sleep(new Random(1000).nextInt());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello demo " + name + "!";
    }

    /**
     * 单元测试
     */
    public static class DemoTest {
        @Test
        public void semaphore() {
            String res = new CommandDemoFailure(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup")).
                    andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                            //是否启用超时
                            .withExecutionTimeoutEnabled(true)
                            //设置超时时间
                            .withExecutionTimeoutInMilliseconds(100)
                            //使用信号量隔离
                            .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                    ), "cc")
                    .execute();
            assertEquals("hello demo", res);

        }

        //使用线程池隔离
        @Test
        public void thread() {

            String res = new CommandDemoFailure(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup")).
                    andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                            //启用超时
                            .withExecutionTimeoutEnabled(true)
                            //设置超时时间
                            .withExecutionTimeoutInMilliseconds(100)
                            //启用断路器
                            .withCircuitBreakerEnabled(true)
                            //失败比例，容错率，当失败比达到 10% 时启用断路器。
                            .withCircuitBreakerErrorThresholdPercentage(10)
                            //当失败请求到达10个开始计算，容错率。
                            .withCircuitBreakerRequestVolumeThreshold(10)
                            //断路器开启后，每 10 秒钟尝试请求，断路器半开状态。
                            .withCircuitBreakerSleepWindowInMilliseconds(10000)
                            //使用线程池隔离
                            .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                    )
                    , "cc").execute();
            assertEquals("hello demo", res);
        }
    }

}
