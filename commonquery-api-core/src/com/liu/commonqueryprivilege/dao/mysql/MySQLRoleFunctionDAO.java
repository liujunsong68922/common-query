package com.liu.commonqueryprivilege.dao.mysql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.liu.commonqueryprivilege.vo.RoleFuncVO;
import com.liu.commonqueryprivilege.vo.UserRoleVO;

//import com.liu.userdataprivilege.core.vo.RoleFuncVO;
//import com.liu.userdataprivilege.core.vo.UserRoleVO;

public class MySQLRoleFunctionDAO {
	private DataSource datasource;

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	private String ROLEFUNCTION_TABLENAME = "T_ROLEFUNCTION";
	private String FUNCTION_TABLENAME = "T_FUNCTION";
	/**
	 * �жϸ�����List��û��ָ��Ȩ��
	 * 
	 * @param userrolelist
	 * @param funcid
	 * @return
	 */
	public boolean checkRoleFunc(List<UserRoleVO> userrolelist, String funcid) {
		String roles = "";
		for (UserRoleVO userrolevo : userrolelist) {
			if (roles.length() == 0) {
				roles += "'" + userrolevo.getRoleid() + "'";
			} else {
				roles += ",'" + userrolevo.getRoleid() + "'";
			}
		}

		JdbcTemplate jdbctemplate = new JdbcTemplate(this.datasource);
		String strsql = "Select roleid,funcid from " + this.ROLEFUNCTION_TABLENAME;
		strsql += " Where roleid in (" + roles + ")";
		strsql += " AND funcid='"+funcid+"'";
		
		System.out.println(strsql);
		List<Map<String, Object>> list1 = jdbctemplate.queryForList(strsql);
		if(list1.size()==0) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * �Բ�ѯFuncIdΪ�ϼ�ID����ѯָ������������Ȩ�޵��б�
	 * @param userrolelist
	 * @param funcid
	 * @return
	 */
	public List<RoleFuncVO> getRoleFunclist(List<UserRoleVO> userrolelist, String funcid){
		String roles = "";
		for (UserRoleVO userrolevo : userrolelist) {
			if (roles.length() == 0) {
				roles += "'" + userrolevo.getRoleid() + "'";
			} else {
				roles += ",'" + userrolevo.getRoleid() + "'";
			}
		}

		JdbcTemplate jdbctemplate = new JdbcTemplate(this.datasource);
		String strsql = "Select a.roleid,a.funcid from " + this.ROLEFUNCTION_TABLENAME +" a," ;
		strsql += this.FUNCTION_TABLENAME +" b";
		strsql += " Where a.funcid=b.funcid ";
		strsql += " AND a.roleid in (" + roles + ")";
		strsql += " AND b.parentfuncid='"+funcid+"'";
		//����һ���ж�������ֻ����functype=3��������Ȩ�޵����
		strsql += " AND b.functype='3'";
		
		System.out.println(strsql);
		
		List<RoleFuncVO> retlist = new ArrayList<RoleFuncVO>();	
		List<Map<String, Object>> list1 = jdbctemplate.queryForList(strsql);
		for(Map<String, Object> map1:list1) {
			RoleFuncVO rolefuncvo = new RoleFuncVO();
			rolefuncvo.setRoleid((String)map1.get("roleid"));
			rolefuncvo.setFuncid((String)map1.get("funcid"));
			retlist.add(rolefuncvo);
		}
		return retlist;
	}
	
	
}
