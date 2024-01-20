package com.flytrap.rssreader.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@EnableScheduling
@DirtiesContext
public class ScheduledServiceTest {

    @Autowired
    private ScheduledService scheduledService;

    @Test
    public void testCollectPostsPerformance() {
        // Adjust the number of iterations based on your needs
        for (int i = 0; i < 10; i++) {
            scheduledService.collectPosts();
        }
    }
}
