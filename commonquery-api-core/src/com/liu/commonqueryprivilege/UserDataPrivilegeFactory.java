package com.liu.commonqueryprivilege;

import com.liu.commonqueryprivilege.impl.db.MySQLUserDataPrivilegeImpl;

//import com.liu.userdataprivilege.core.impl.db.MySQLUserDataPrivilegeImpl;

//import com.liu.userdataprivilege.core.impl.db.MySQLUserDataPrivilegeImpl;

/**
 * ������
 * @author liujunsong
 *
 */
public class UserDataPrivilegeFactory {
	public IUserDataPrivilege getUserDataPrivilege(String type) {
		if(type.equalsIgnoreCase("mysql")) {
			return new MySQLUserDataPrivilegeImpl();
		}
		
		System.out.println("Unsupported type:"+type);
		return null;
	}
}
