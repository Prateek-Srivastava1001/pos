package com.increff.pos.dto;

import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.ConverterUtil;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Component
public class UserDto {
    @Value("${app.supervisor}")
    private String supervisor;
    @Autowired
    private UserService service;
    @Autowired
    private InfoData info;
    @Autowired
    private ConverterUtil util;
    @Autowired
    private NormalizeUtil normalizeUtil;

    public ModelAndView login(HttpServletRequest req, LoginForm f) throws ApiException {
        UserPojo p = service.get(f.getEmail());
        boolean authenticated = (p != null && Objects.equals(p.getPassword(), f.getPassword()));
        if (!authenticated) {
            info.setMessage("Invalid details");
            throw new ApiException("Invalid details");
        }
        info.setRole(p.getRole());
        // Create authentication object
        Authentication authentication = util.convert(p);
        // Create new session
        HttpSession session = req.getSession(true);
        // Attach Spring SecurityContext to this new session
        SecurityUtil.createContext(session);
        // Attach Authentication object to the Security Context
        SecurityUtil.setAuthentication(authentication);

        return new ModelAndView("redirect:/ui/home");

    }

    public ModelAndView signup(HttpServletRequest req, LoginForm form) throws ApiException{
        String[] array = supervisor.split(",");
        UserPojo p= util.convert(form, array);
        normalizeUtil.normalize(p);
        boolean success = service.add(p);

        if(!success){
            info.setMessage("Email already exists");
            throw new ApiException("Email already exists");
        }
        ModelAndView mav = login(req, form);
        return mav;
    }
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return new ModelAndView("redirect:/site/logout");
    }

}
