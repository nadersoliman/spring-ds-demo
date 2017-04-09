package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@TestConfiguration
@Profile("test")
public class Populator {

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    ResourcePatternResolver resourceResolver;

    @Bean
    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() throws Exception {
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        factory.setMapper(jsonMapper);
        factory.setResources(resourceResolver.getResources("classpath:test-data/*.json"));
        factory.afterPropertiesSet();
        return factory;
    }
}
