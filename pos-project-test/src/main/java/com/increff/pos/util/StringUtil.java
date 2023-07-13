package com.increff.pos.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {

	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

}
