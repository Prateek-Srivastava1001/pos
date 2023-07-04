package com.increff.pos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AboutAppService {

	@Value("${app.name}")
	private String name;
	@Value("${app.version}")
	
	private String version;

	@Value("${app.supervisor}")
	private String supervisor;


	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getSupervisor() {
		return supervisor;
	}
}
