package com.restful.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        basePackages = "com.restful.api.repository.amazon",
        entityManagerFactoryRef = "amazonMovieEntityManager", //manages communication with DB
        transactionManagerRef = "amazonMovieTransactionManager"
)
public class PersistenceAmazonMovieConfiguration {

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean amazonMovieEntityManager() { //create the entity manager in the application
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("amazon");
        em.setDataSource(amazonMovieDataSource());
        em.setPackagesToScan("com.restful.api.domain.amazon");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "validate");
        properties.put("hibernate.dialect", "com.restful.api.sql.SQLiteDialect");
        properties.put("hibernate.show_sql", "true");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    public DataSource amazonMovieDataSource() { //find the amazon DB

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:amazon.db");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        return dataSource;

    }

    @Primary
    @Bean
    public PlatformTransactionManager amazonMovieTransactionManager() {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(amazonMovieEntityManager().getObject());
        return transactionManager;

    }
}
