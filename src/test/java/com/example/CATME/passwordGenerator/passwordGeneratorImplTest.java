package com.example.CATME.passwordGenerator;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class passwordGeneratorImplTest {

    private final PasswordGenerator passwordGenerator = new PasswordGeneratorImpl();
    @Test
    public void generatePasswordTest(){
        String password = passwordGenerator.generatePassword();
        assertNotNull(password);
        assertEquals(password.length(), 10);
    }
}