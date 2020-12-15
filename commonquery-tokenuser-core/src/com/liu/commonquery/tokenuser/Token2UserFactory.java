package com.liu.commonquery.tokenuser;

import com.liu.commonquery.tokenuser.impl.mysql.MySQLIToken2UserImpl;

public class Token2UserFactory {
	/**
	 * 获取接口的具体实现类
	 * @param type
	 * @return
	 */
	public IToken2User getIToken2User(String type) {
		if(type.equalsIgnoreCase("mysql")) {
			return new MySQLIToken2UserImpl();
		}
		System.out.println("Unsupported type:"+type);
		return null;
	}
}
