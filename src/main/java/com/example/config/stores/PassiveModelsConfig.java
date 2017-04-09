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
        entityManagerFactoryRef = "passiveModelsEntityManager",
        transactionManagerRef = "passiveModelsTransactionManager",
        basePackages = {"com.example.models.passive"})
public class PassiveModelsConfig {
    private static final Logger LOG = LoggerFactory.getLogger(PassiveModelsConfig.class);

    @Autowired(required = false)
    private PersistenceUnitManager persistenceUnitManager;

    @Bean
    @ConfigurationProperties("app.passive-models.jpa")
    public JpaProperties passiveModelsJpaProperties() {
        return new JpaProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "app.passive-models.datasource")
    public DataSource passiveModelsDataSource() {
        return (DataSource) DataSourceBuilder.create().type(DataSource.class).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean passiveModelsEntityManager(
            JpaProperties passiveModelsJpaProperties) {
        EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(passiveModelsJpaProperties);
        return builder
                .dataSource(passiveModelsDataSource())
                .packages("com.example.models.passive")
                .persistenceUnit("passiveModelssDs")
                .build();
    }

    @Bean
    public JpaTransactionManager passiveModelsTransactionManager(EntityManagerFactory passiveModelsEntityManager) {
        return new JpaTransactionManager(passiveModelsEntityManager);
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties passiveModelsJpaProperties) {
        JpaVendorAdapter jpaVendorAdapter = createJpaVendorAdapter(passiveModelsJpaProperties);
        return new EntityManagerFactoryBuilder(jpaVendorAdapter,
                passiveModelsJpaProperties.getProperties(), this.persistenceUnitManager);
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
