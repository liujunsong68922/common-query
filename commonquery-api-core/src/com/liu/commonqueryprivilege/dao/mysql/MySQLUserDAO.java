package com.liu.commonqueryprivilege.dao.mysql;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.liu.commonqueryprivilege.vo.UserVO;

//import com.liu.userdataprivilege.core.vo.UserVO;

public class MySQLUserDAO {
	private DataSource datasource;

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	private String USER_TABLENAME = "T_USER";

	public UserVO getUserVO(String userid) {
		String strsql = "Select userid from " + this.USER_TABLENAME;
		strsql += " Where userid='" + userid + "'";

		JdbcTemplate jdbctemplate = new JdbcTemplate(this.datasource);
		List<Map<String, Object>> list1 = jdbctemplate.queryForList(strsql);
		if (list1.size() == 0) {
			return null;
		} else {
			Map<String, Object> map1 = list1.get(0);
			UserVO uservo = new UserVO();
			uservo.setUserid(userid);
			return uservo;
		}
	}
}
