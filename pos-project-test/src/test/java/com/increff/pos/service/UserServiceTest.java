package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.UserPojo;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class UserServiceTest extends AbstractUnitTest {
    @Autowired
    UserService service;

    //test adding user and getting by email
    @Test
    public void testAdd() throws ApiException{
        UserPojo pojo = new UserPojo();
        pojo.setEmail("random@gmail.com");
        pojo.setPassword("password");
        pojo.setRole("supervisor");
        boolean success = service.add(pojo);
        assertEquals(true, success);

        UserPojo gotPojo = service.get("random@gmail.com");
        assertEquals("random@gmail.com", gotPojo.getEmail());
        assertEquals("password", gotPojo.getPassword());
        assertEquals("supervisor", gotPojo.getRole());
    }
    // adding empty email
    @Test
    public void testAddEmptyEmail() throws ApiException{
        try {
            UserPojo pojo = new UserPojo();
            pojo.setEmail("");
            pojo.setPassword("password");
            pojo.setRole("supervisor");
            service.add(pojo);
        } catch (ApiException err){
            TestCase.assertEquals("Email cannot be empty", err.getMessage());
        }
    }
    //adding empty password
    @Test
    public void testAddEmptyPassword() throws ApiException{
        try {
            UserPojo pojo = new UserPojo();
            pojo.setEmail("random@gmail.com");
            pojo.setPassword("");
            pojo.setRole("supervisor");
            boolean success = service.add(pojo);
        } catch (ApiException err){
            TestCase.assertEquals("Password cannot be empty", err.getMessage());
        }
    }
    //adding pre-existing user
    @Test
    public void testAddExistingUser() throws ApiException{
        UserPojo pojo = new UserPojo();
        pojo.setEmail("random@gmail.com");
        pojo.setPassword("password");
        pojo.setRole("supervisor");
        service.add(pojo);

        UserPojo newPojo = new UserPojo();
        newPojo.setEmail("random@gmail.com");
        newPojo.setPassword("newPassword");
        newPojo.setRole("supervisor");
        boolean success = service.add(newPojo);

        assertEquals(false, success);
    }
}
