package com.restful.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.restful.api.repository.netflix",
        entityManagerFactoryRef = "netflixMovieEntityManager", //manages communication with DB
        transactionManagerRef = "netflixMovieTransactionManager"
)
public class PersistenceNetflixMovieConfiguration {

    @Bean
    public LocalContainerEntityManagerFactoryBean netflixMovieEntityManager() { //create the entity manager in the application
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("netflix");
        em.setDataSource(netflixMovieDataSource());
        em.setPackagesToScan("com.restful.api.domain.netflix");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "validate");
        properties.put("hibernate.dialect", "com.restful.api.sql.SQLiteDialect"); //the database
        properties.put("hibernate.show_sql", "true");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public DataSource netflixMovieDataSource() { //where to find the data source

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:netflix.db");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        return dataSource;

    }

    @Bean
    public PlatformTransactionManager netflixMovieTransactionManager() {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(netflixMovieEntityManager().getObject()); //getting the object
        return transactionManager;

    }

}
