package me.dong.spocksample.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import me.dong.spocksample.OrderRepository;

import static me.dong.spocksample.batch.OrderJobConfiguration.JOB_NAME;

/**
 * reference - http://jojoldu.tistory.com/229?category=635881
 * <p>
 * Created by ethan.kim on 2018. 8. 22..
 */
@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = JOB_NAME)
@Slf4j
public class OrderJobConfiguration {

    public static final String JOB_NAME = "orderJob";

    private static final String STEP_NAME = "orderStep";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public OrderJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job job(Step step) {
        return jobBuilderFactory.get(JOB_NAME)
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step step(Tasklet tasklet) {
        return stepBuilderFactory.get(STEP_NAME)
                .tasklet(tasklet)
                .build();
    }

    @Autowired
    private OrderRepository orderRepository;

    @Bean
    @StepScope
    public Tasklet tasklet() {
        return (contribution, chunkContext) -> {
            orderRepository.findOne(1L);
            log.info("tasklet execute");
            return RepeatStatus.FINISHED;
        };
    }
}
