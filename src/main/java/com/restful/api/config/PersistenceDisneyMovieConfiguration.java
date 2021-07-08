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
        basePackages = "com.restful.api.repository.disney",
        entityManagerFactoryRef = "disneyMovieEntityManager", //manages communication with DB
        transactionManagerRef = "disneyMovieTransactionManager"
)
public class PersistenceDisneyMovieConfiguration {


    @Bean
    public LocalContainerEntityManagerFactoryBean disneyMovieEntityManager() { //create the entity manager in the application
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("netflix");
        em.setDataSource(disneyMovieDataSource());
        em.setPackagesToScan("com.restful.api.domain.disney");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "validate");
        properties.put("hibernate.dialect", "com.restful.api.sql.SQLiteDialect");
        properties.put("hibernate.show_sql", "true");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public DataSource disneyMovieDataSource() { //where to find the source with the DB

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:disney.db");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        return dataSource;

    }

    @Bean
    public PlatformTransactionManager disneyMovieTransactionManager() { //transaction from the object to get the object.

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(disneyMovieEntityManager().getObject());
        return transactionManager;

    }


}
