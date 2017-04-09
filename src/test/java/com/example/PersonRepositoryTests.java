package com.example;

import com.example.models.active.PersonRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Populator.class })
public class PersonRepositoryTests {

    @Autowired
    PersonRepository personRepository;

    @Test
    public void twoPersons() {

        Assert.assertEquals(2, personRepository.count());

    }
}
