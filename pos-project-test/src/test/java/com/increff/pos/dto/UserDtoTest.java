package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.helper.FormHelper;
import com.increff.pos.model.LoginForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;


import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;

public class UserDtoTest extends AbstractUnitTest {
    @Autowired
    UserDto dto;

    //Signup Tests
    @Test
    public void testSignUp() throws ApiException{
        LoginForm form = FormHelper.createUser("random@gmail.com", "password1234");
        UserPojo pojo = dto.signup(form);
        String expectedEmail = "random@gmail.com";
        String expectedPassword = "password1234";
        String expectedRole = "operator";
        assertEquals(expectedEmail, pojo.getEmail());
        assertEquals(expectedPassword, pojo.getPassword());
        assertEquals(expectedRole, pojo.getRole());

        //checking supervisor role
        LoginForm supervisorForm = FormHelper.createUser("prateek@gmail.com", "prateek1234");
        UserPojo newUser = dto.signup(supervisorForm);
        assertEquals("supervisor", newUser.getRole());
    }
    //Email already exists
    @Test
    public void testExistingEmail() throws ApiException{
        try {
            LoginForm form = FormHelper.createUser("random@gmail.com", "password1234");
            dto.signup(form);
            dto.signup(form);
        } catch (ApiException err){
            Assert.assertEquals("Email already exists", err.getMessage());
        }
    }
    //Empty email
    @Test
    public void testEmptyEmail() throws ApiException{
        try {
            LoginForm form = FormHelper.createUser("", "password1234");
            dto.signup(form);
        } catch (ApiException err){
            Assert.assertEquals("Email cannot be empty", err.getMessage());
        }
    }
    //Empty password
    @Test
    public void testEmptyPassword() throws ApiException{
        try {
            LoginForm form = FormHelper.createUser("random@gmail.com", "");
            dto.signup(form);
        } catch (ApiException err){
            Assert.assertEquals("Password cannot be empty", err.getMessage());
        }
    }

    // LOGIN METHOD TESTS...
    @Test
    public void testLogin() throws ApiException{
        LoginForm form = FormHelper.createUser("raNdom@gmail.com", "password1234");
        dto.signup(form);
        Authentication authentication = dto.login(form);
        assertNotNull(authentication);
    }
    // Invalid password
    @Test
    public void testInvalidPassword() throws ApiException{
        try {
            LoginForm form = FormHelper.createUser("random@gmail.com", "password1234");
            dto.signup(form);
            LoginForm invalidForm = FormHelper.createUser("random@gmail.com", "Password1234");
            dto.login(invalidForm);
        } catch (ApiException err){
            Assert.assertEquals("Invalid details", err.getMessage());
        }
    }
    // Invalid Email (User does not exist)
    @Test
    public void testInvalidEmail() throws ApiException{
        try {
            LoginForm form = FormHelper.createUser("random@gmail.com", "password1234");
            dto.signup(form);
            LoginForm invalidForm = FormHelper.createUser("invalid@gmail.com", "password1234");
            dto.login(invalidForm);
        } catch (ApiException err){
            Assert.assertEquals("Invalid details", err.getMessage());
        }
    }

}
