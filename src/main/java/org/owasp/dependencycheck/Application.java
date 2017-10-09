package org.owasp.dependencycheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * Hello world!
 *
 */


@SpringBootApplication
@EnableJpaRepositories
@Import(RepositoryRestMvcAutoConfiguration.class)
@EnableAutoConfiguration
public class Application
{
    public static void main( String[] args )
    {
        SpringApplication.run(Application.class, args);
    }
}
