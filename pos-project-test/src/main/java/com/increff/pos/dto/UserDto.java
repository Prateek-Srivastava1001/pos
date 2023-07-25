package com.increff.pos.dto;
import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.ConverterUtil;
import com.increff.pos.util.NormalizeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Objects;
@Component
public class UserDto{
    @Value("${app.supervisor}")
    private String supervisor;
    @Autowired
    private UserService service;
    @Autowired
    private InfoData info;
    private ConverterUtil util;
    private NormalizeUtil normalizeUtil;
    // checks if the details are valid and returns an authentication object
    public Authentication login(LoginForm f) throws ApiException{
        f.setEmail(f.getEmail().toLowerCase().trim());
        UserPojo p = service.get(f.getEmail());
        boolean authenticated = (p != null && Objects.equals(p.getPassword(), f.getPassword()));
        if (!authenticated) {
            info.setMessage("Invalid details");
            throw new ApiException("Invalid details");
        }
        info.setRole(p.getRole());
        return util.convert(p);
    }
    // adds new user in db where role comes from pos.properties file
    public UserPojo signup(LoginForm form) throws ApiException{
        String[] array = supervisor.split(",");
        UserPojo p= util.convert(form, array);
        normalizeUtil.normalize(p);
        boolean success = service.add(p);
        if(!success){
            info.setMessage("Email already exists");
            throw new ApiException("Email already exists");}
        return p;
    }
}
