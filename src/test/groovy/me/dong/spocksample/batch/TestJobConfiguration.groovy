package me.dong.spocksample.batch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

/**
 * Created by ethan.kim on 2018. 8. 22..
 */
@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@Component
class TestJobConfiguration {

    @Bean
    JobLauncherTestUtils jobLauncherTestUtils() {
        return new JobLauncherTestUtils()
    }
}
