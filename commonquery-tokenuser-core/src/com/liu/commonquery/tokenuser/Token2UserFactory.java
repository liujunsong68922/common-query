package com.liu.commonquery.tokenuser;

import com.liu.commonquery.tokenuser.impl.mysql.MySQLIToken2UserImpl;

public class Token2UserFactory {
	/**
	 * ��ȡ�ӿڵľ���ʵ����
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
