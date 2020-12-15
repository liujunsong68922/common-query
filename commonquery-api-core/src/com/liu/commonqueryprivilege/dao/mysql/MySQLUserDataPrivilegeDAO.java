package com.liu.commonqueryprivilege.dao.mysql;

import javax.sql.DataSource;

import com.liu.commonqueryprivilege.vo.UserVO;

//import com.liu.userdataprivilege.core.vo.UserVO;

public class MySQLUserDataPrivilegeDAO {
	private DataSource datasource ;

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}
	
	public UserVO getUserVO(String userid) {
		String strsql="Select userid from t_user ";
		return null;
	}
}
