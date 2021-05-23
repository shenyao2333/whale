package com.whale.provider.basices.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @author sy
 * @date: 2021/2/5 10:01
 * @description
 */


@Slf4j
@Component
public class ExecutorConfig {



    @Bean(name = "threadPool")
    public static ExecutorService threadPool() {
        int max = Runtime.getRuntime().availableProcessors();
        log.info("初始化线程数："+max);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(max / 2 + 1, max,
                60, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        return threadPoolExecutor;
    }


    public static void main(String[] args) {




    }




}
