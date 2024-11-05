package com.noblesse.backend.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = {
                "com.noblesse.backend.admin.repository",
                "com.noblesse.backend.clip.repository",
                "com.noblesse.backend.file.repository",
                "com.noblesse.backend.follow.repository",
                "com.noblesse.backend.manage.user.repository",
                "com.noblesse.backend.manage.clip.repository",
                "com.noblesse.backend.notice.repository",
                "com.noblesse.backend.oauth2.repository",
                "com.noblesse.backend.post.query.infrastructure.persistence.repository",
                "com.noblesse.backend.preference.repository",
                "com.noblesse.backend.trip.repository"
        },
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class MainDatabaseConfig {

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("dataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(
                "com.noblesse.backend.admin.entity",
                "com.noblesse.backend.bookmark",
                "com.noblesse.backend.clip.domain",
                "com.noblesse.backend.file.entity",
                "com.noblesse.backend.follow.domain",
                "com.noblesse.backend.notice.domain",
                "com.noblesse.backend.oauth2.entity",
                "com.noblesse.backend.post.common.entity",
                "com.noblesse.backend.preference.domain",
                "com.noblesse.backend.trip.domain",
                "com.noblesse.backend.manage.clip.domain"
        );

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }
}
