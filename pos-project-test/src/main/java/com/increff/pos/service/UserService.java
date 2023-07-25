package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.model.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;

@Service
public class UserService{
	@Autowired
	private UserDao dao;
	//CREATE
	@Transactional
	public boolean add(UserPojo p) throws ApiException {
		if(p.getEmail()==null || p.getEmail() == ""){
			throw new ApiException("Email cannot be empty");
		}
		if(p.getPassword()==null || p.getPassword() == ""){
			throw new ApiException("Password cannot be empty");
		}
		UserPojo existing = dao.select(p.getEmail());
		if (existing != null) {
			return false;
		}
		dao.insert(p);
		return true;
	}
	//READ
	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}


}
