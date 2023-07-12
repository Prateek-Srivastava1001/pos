package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.helper.FormHelper;
import com.increff.pos.model.LoginForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
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
    @Test(expected = ApiException.class)
    public void testExistingEmail() throws ApiException{
        LoginForm form = FormHelper.createUser("random@gmail.com", "password1234");
        dto.signup(form);
        dto.signup(form);
    }
    //Empty email
    @Test(expected = ApiException.class)
    public void testEmptyEmail() throws ApiException{
        LoginForm form = FormHelper.createUser("", "password1234");
        dto.signup(form);
    }
    //Empty password
    @Test(expected = ApiException.class)
    public void testEmptyPassword() throws ApiException{
        LoginForm form = FormHelper.createUser("random@gmail.com", "");
        dto.signup(form);
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
    @Test(expected = ApiException.class)
    public void testInvalidPassword() throws ApiException{
        LoginForm form = FormHelper.createUser("random@gmail.com", "password1234");
        dto.signup(form);
        LoginForm invalidForm = FormHelper.createUser("random@gmail.com", "Password1234");
        dto.login(invalidForm);
    }
    // Invalid Email (User does not exist)
    @Test(expected = ApiException.class)
    public void testInvalidEmail() throws ApiException{
        LoginForm form = FormHelper.createUser("random@gmail.com", "password1234");
        dto.signup(form);
        LoginForm invalidForm = FormHelper.createUser("invalid@gmail.com", "password1234");
        dto.login(invalidForm);
    }

}
