package com.ecommerce.admin.user;

import com.ecommerce.common.entity.Role;
import com.ecommerce.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUserWithOneRole(){
        Role roleAdmin = entityManager.find(Role.class,1);
        User userHuso = new User("test@test.com","test123","huseyin","cakir");
        userHuso.addRole(roleAdmin);
        User savedUser = userRepository.save(userHuso);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateUserWithTwoRoles(){

        User userAhmet = new User("test2@test.com","test123","ahmet","cakir");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);
        userAhmet.addRole(roleEditor);
        userAhmet.addRole(roleAssistant);
        User savedUser = userRepository.save(userAhmet);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers(){
        Iterable<User> listUsers = userRepository.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById(){

        User userHuso = userRepository.findById(1).get();
        System.out.println(userHuso);
        assertThat(userHuso.getId()).isGreaterThan(0);
    }

    @Test
    public void testUpdateUserDetails(){

        User userHuso = userRepository.findById(1).get();
        userHuso.setEnabled(true);
        userHuso.setEmail("test4@test.com");

    }
    @Test
    public void testUpdateUserRoles(){

        User userAhmet = userRepository.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSalesPerson = new Role(2);

        userAhmet.getRoles().remove(roleEditor);
        userAhmet.addRole((roleSalesPerson));
        userRepository.save(userAhmet);

    }

    @Test
    public void testDeleteUser(){

        Integer userId = 2;
        userRepository.deleteById(userId);

    }
}
