package com.flytrap.rssreader.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "alertThreadExecutor")
    public ThreadPoolTaskExecutor alertThreadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setThreadNamePrefix("alertThreadExecutor-");
        executor.initialize();
        return executor;
    }
    @Bean(name = "asyncTaskExecutor")
    public ThreadPoolTaskExecutor asyncTaskExecutor () {
        ThreadPoolTaskExecutor  executor  =  new  ThreadPoolTaskExecutor ();
        executor.setCorePoolSize( 1 ); // 풀의 초기 스레드 수를 설정합니다.
        executor.setMaxPoolSize( 5 ); // 풀의 최대 스레드 수를 설정합니다.
      //  executor.setQueueCapacity( 20 ); // 보류 중인 작업을 보관하기 위한 대기열 용량 설정
        executor.setThreadNamePrefix( "AsyncTask-" ); // 스레드 이름에 대한 접두사 설정
        executor.initialize();
        return executor;
    }
}
