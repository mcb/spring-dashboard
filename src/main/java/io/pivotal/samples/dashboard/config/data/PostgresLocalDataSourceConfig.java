package io.pivotal.samples.dashboard.config.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("postgres-local")
public class PostgresLocalDataSourceConfig extends AbstractLocalDataSourceConfig {

    @Bean
    public DataSource dataSource() {
        return createDataSource("jdbc:postgresql://localhost/dashboard",
                "org.postgresql.Driver", "postgres", "postgres");
    }

}
