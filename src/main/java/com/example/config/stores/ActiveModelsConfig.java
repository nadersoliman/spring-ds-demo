package com.example.config.stores;


import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "activeModelsEntityManager",
        transactionManagerRef = "activeModelsTransactionManager",
        basePackages = { "com.example.models.active" })
public class ActiveModelsConfig {
    private static final Logger LOG = LoggerFactory.getLogger(ActiveModelsConfig.class);

    @Autowired(required = false)
    private PersistenceUnitManager persistenceUnitManager;

    @Bean
    @Primary
    @ConfigurationProperties("app.active-models.jpa")
    public JpaProperties activeModelsJpaProperties() {
        return new JpaProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "app.active-models.datasource")
    public DataSource activeModelsDataSource() {
        return (DataSource) DataSourceBuilder.create().type(DataSource.class).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean activeModelsEntityManager(
            JpaProperties activeModelsJpaProperties) {
        EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(activeModelsJpaProperties);
        return builder
                .dataSource(activeModelsDataSource())
                .packages("com.example.models.active")
                .persistenceUnit("activeModelssDs")
                .build();
    }

    @Bean
    @Primary
    public JpaTransactionManager activeModelsTransactionManager(EntityManagerFactory activeModelsEntityManager) {
        return new JpaTransactionManager(activeModelsEntityManager);
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties activeModelsJpaProperties) {
        JpaVendorAdapter jpaVendorAdapter = createJpaVendorAdapter(activeModelsJpaProperties);
        return new EntityManagerFactoryBuilder(jpaVendorAdapter,
                activeModelsJpaProperties.getProperties(), this.persistenceUnitManager);
    }

    private JpaVendorAdapter createJpaVendorAdapter(JpaProperties jpaProperties) {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(jpaProperties.isShowSql());
        adapter.setDatabase(jpaProperties.getDatabase());
        adapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
        adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
        return adapter;
    }
}
