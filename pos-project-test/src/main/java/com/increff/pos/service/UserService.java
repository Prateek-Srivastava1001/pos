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
	private AboutAppService service;
	@Autowired
	private UserDao dao;

	@Transactional
	public boolean add(LoginForm form) throws ApiException {
		UserPojo p= convert(form);
		if(p.getPassword()==null || p.getPassword() == ""){
			throw new ApiException("Password cannot be empty");
		}
		normalize(p);

		UserPojo existing = dao.select(p.getEmail());
		if (existing != null) {
			return false;
		}

		dao.insert(p);

		return true;
	}

	@Transactional(rollbackOn = ApiException.class)
	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}

	@Transactional
	public List<UserPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	protected UserPojo convert(LoginForm form){
		int flag=0;
		UserPojo pojo = new UserPojo();
		pojo.setEmail(form.getEmail());
		pojo.setPassword(form.getPassword());
		String[] array = service.getSupervisor().split(",");
		for(String supervisor: array){
			if(supervisor.equals(pojo.getEmail()))
				flag=1;
		}
		if(flag==1){
			pojo.setRole("supervisor");
			System.out.println("Supervisor role assigned");
		}
		else{
			pojo.setRole("operator");
			System.out.println("operator role assigned");
		}
		return pojo;
	}
	protected static void normalize(UserPojo p) {
		p.setEmail(p.getEmail().toLowerCase().trim());
		p.setRole(p.getRole().toLowerCase().trim());
	}
}
