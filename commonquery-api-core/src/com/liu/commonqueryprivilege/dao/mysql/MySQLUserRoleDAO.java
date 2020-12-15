package com.liu.commonqueryprivilege.dao.mysql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.liu.commonqueryprivilege.vo.UserRoleVO;

//import com.liu.userdataprivilege.core.vo.UserRoleVO;

public class MySQLUserRoleDAO {
	private DataSource datasource;

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}
	
	private String USERROLE_TABLENAME = "T_USERROLE";
	/**
	 * ����userId��ȡRoleId�б�
	 * @param userid
	 * @return
	 */
	public List<UserRoleVO> getUserRoleList(String userid){
		JdbcTemplate jdbctemplate = new JdbcTemplate(this.datasource);
		String strsql = "Select userid,roleid from " + this.USERROLE_TABLENAME;
		strsql += " Where userid='" + userid + "'";		

		List<UserRoleVO> retlist = new ArrayList<UserRoleVO>();
		
		List<Map<String, Object>> list1 = jdbctemplate.queryForList(strsql);
		if(list1.size()==0) {
			return retlist;
		}else {
			for(Map<String, Object> map1:list1) {
				UserRoleVO userrolevo = new UserRoleVO();
				userrolevo.setUserid((String)map1.get("userid"));
				userrolevo.setRoleid((String)map1.get("roleid"));
				retlist.add(userrolevo);
			}
		}
		return retlist;
	}
}
