package com.yutsuki.chatserver.repository;

import com.github.javafaker.Faker;
import com.yutsuki.chatserver.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final Faker faker = new Faker();

    private User randomUser() {
        User user = new User();
        user.setName(faker.name().fullName());
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(faker.internet().password());
        user.setVerified(Boolean.TRUE);
        return user;
    }

    @Test
    @Order(1)
    void saveUser() {

        // when
        User user = userRepository.save(randomUser());

        // then
        assertNotNull(user.getId());
    }

    @Test
    @Order(2)
    void findUserByEmail() {

        // given
        User user = userRepository.save(randomUser());

        // when
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());

        // then
        assertTrue(foundUser.isPresent());
    }

    @Test
    @Order(3)
    void updateUser() {

        // given
        User user = userRepository.save(randomUser());

        // when
        user.setName(faker.name().fullName());
        userRepository.save(user);
        User updatedUser = userRepository.findByEmail(user.getEmail()).get();

        // then
        assertEquals(user.getName(), updatedUser.getName());
    }

    @Test
    @Order(4)
    void deleteUser() {

        // given
        User user = userRepository.save(randomUser());

        // when
        userRepository.delete(user);

        // then
        assertFalse(userRepository.findByEmail(user.getEmail()).isPresent());
    }
}
