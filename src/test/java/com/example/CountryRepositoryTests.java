package com.example;

import com.example.models.passive.CountryRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Populator.class})
@DirtiesContext
public class CountryRepositoryTests {

    @Autowired
    CountryRepository countryRepository;

    @Test
    public void twoCountries() {

        Assert.assertEquals(2, countryRepository.count());

    }
}
