package pl.dariuszgilewicz.configuration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;
import org.testcontainers.containers.PostgreSQLContainer;

class DatabaseContainerInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public static final String POSTGRESQL_USERNAME = "username";
    public static final String POSTGRESQL_PASSWORD = "password";
    public static final String POSTGRESQL_BEAN_NAME = "postgres";
    public static final String POSTGRESQL_CONTAINER = "postgres:15.0";

    @Override
    @SuppressWarnings("resource")
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>(POSTGRESQL_CONTAINER)
                .withUsername(POSTGRESQL_USERNAME)
                .withPassword(POSTGRESQL_PASSWORD);
        container.start();
        System.setProperty("spring.datasource.url", container.getJdbcUrl());
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());
        applicationContext.addApplicationListener(event -> {
            if (event instanceof ContextClosedEvent) {
                container.close();
            }
        });
        applicationContext.getBeanFactory().registerSingleton(POSTGRESQL_BEAN_NAME, container);
    }
}
