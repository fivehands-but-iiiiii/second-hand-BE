package com.team5.secondhand;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;

@Testcontainers
@ContextConfiguration(initializers = TestContainer.ContainerPropertyInitializer.class)
public abstract class TestContainer {

    static DockerComposeContainer composeContainer =
            new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("redis", 6379, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)))
                    .withExposedService("mysql", 3306, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)))
                    .withExposedService("mongodb", 27017, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));

    static {
        composeContainer.start();
    }

    static class ContainerPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of("container.port=" + composeContainer.getServicePort("mysql", 3306))
                    .applyTo(context);
            TestPropertyValues.of("container.port=" + composeContainer.getServicePort("redis", 6379))
                    .applyTo(context);
            TestPropertyValues.of("container.port=" + composeContainer.getServicePort("mongodb", 27017))
                    .applyTo(context);
        }
    }

}
