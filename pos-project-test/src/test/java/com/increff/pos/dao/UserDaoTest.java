package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.UserPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class UserDaoTest extends AbstractUnitTest {
    @Autowired
    UserDao dao;

    // insert and select methods test
    @Test
    public void testUserDao(){
        UserPojo pojo = new UserPojo();
        pojo.setEmail("random@gmail.com");
        pojo.setPassword("password");
        pojo.setRole("supervisor");
        dao.insert(pojo);

        UserPojo gotPojo = dao.select("random@gmail.com");

        assertEquals("random@gmail.com", gotPojo.getEmail());
        assertEquals("password", gotPojo.getPassword());
        assertEquals("supervisor", gotPojo.getRole());
    }
}
