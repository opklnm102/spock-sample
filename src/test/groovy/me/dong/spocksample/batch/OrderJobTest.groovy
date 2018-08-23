package me.dong.spocksample.batch

import me.dong.spocksample.OrderRepository
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import javax.batch.runtime.BatchStatus

/**
 * Created by ethan.kim on 2018. 8. 23..
 */
@SpringBootTest
@TestPropertySource(properties = "job.name=orderJob")
class OrderJobTest extends Specification {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils

    @MockBean
    OrderRepository orderRepository

    def "orderJob test"() {
        given:
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("version", new Date())
                .toJobParameters()

        when:
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters)

        then:
        jobExecution.status == BatchStatus.COMPLETED
        1 * orderRepository.findOne(_)
    }
}
