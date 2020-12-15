package com.liu.commonqueryprivilege.impl.db;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.liu.commonqueryprivilege.IUserDataPrivilege;
import com.liu.commonqueryprivilege.UserDataPrivilegeResultVO;
import com.liu.commonqueryprivilege.dao.mysql.*;
import com.liu.commonqueryprivilege.vo.*;
//import com.liu.userdataprivilege.core.UserDataPrivilegeResultVO;

//import com.liu.userdataprivilege.core.IUserDataPrivilege;
//import com.liu.userdataprivilege.core.UserDataPrivilegeResultVO;
//import com.liu.userdataprivilege.core.dao.mysql.MySQLFuncConditionDAO;
//import com.liu.userdataprivilege.core.dao.mysql.MySQLFuncDAO;
//import com.liu.userdataprivilege.core.dao.mysql.MySQLRoleFunctionDAO;
//import com.liu.userdataprivilege.core.dao.mysql.MySQLUserDAO;
//import com.liu.userdataprivilege.core.dao.mysql.MySQLUserDataPrivilegeDAO;
//import com.liu.userdataprivilege.core.dao.mysql.MySQLUserRoleDAO;
//import com.liu.userdataprivilege.core.vo.FuncVO;
//import com.liu.userdataprivilege.core.vo.RoleFuncVO;
//import com.liu.userdataprivilege.core.vo.UserRoleVO;
//import com.liu.userdataprivilege.core.vo.UserVO;

public class MySQLUserDataPrivilegeImpl implements IUserDataPrivilege {
	private DataSource datasource;

	/**
	 * 获取数据权限的查询结果
	 */
	@Override
	public UserDataPrivilegeResultVO getDataPrivilegeResultVO(String userid, String queryfuncid) {
		UserDataPrivilegeResultVO resultvo = new UserDataPrivilegeResultVO();
		
		// define dao
		MySQLUserDataPrivilegeDAO dao = new MySQLUserDataPrivilegeDAO();
		// set datasource
		dao.setDatasource(this.datasource);
		
		//step1:check userid
		MySQLUserDAO userdao = new MySQLUserDAO();
		userdao.setDatasource(this.datasource);
		UserVO uservo= userdao.getUserVO(userid);
		if(uservo == null) {
			resultvo.setIret(-1);
			resultvo.setStrCondition("");
			resultvo.setErrMsg("error Userid:"+userid);
			return resultvo;
		}
		
		//step2:get roleid from userid
		MySQLUserRoleDAO userroledao = new MySQLUserRoleDAO();
		userroledao.setDatasource(this.datasource);
		List<UserRoleVO> userrolelist = userroledao.getUserRoleList(userid);
		if(userrolelist.size()==0) {
			resultvo.setIret(-1);
			resultvo.setStrCondition("");
			resultvo.setErrMsg("cannot get RoleIds,userId:"+userid);
			return resultvo;			
		}
		
		//step3:check user's query functionid
		MySQLRoleFunctionDAO rolefunctiondao = new MySQLRoleFunctionDAO();
		rolefunctiondao.setDatasource(this.datasource);
		
		boolean checkflag = rolefunctiondao.checkRoleFunc(userrolelist, queryfuncid);
		if(!checkflag) {
			resultvo.setIret(-1);
			resultvo.setStrCondition("");
			resultvo.setErrMsg("User Query Not Authoried.QueryFuncId,userId:"+userid+",qfuncid:"+queryfuncid);
			return resultvo;			
		}
		
		//step4:����RoleID���б���ѯִ��queryfuncid����������Ȩ���б�
		List<RoleFuncVO> rolefunclist = rolefunctiondao.getRoleFunclist(userrolelist, queryfuncid);
		
		if(rolefunclist.size()==0) {
			System.out.println("Cannot find funclist.userid,funcid="+userid+","+queryfuncid);
			resultvo.setIret(-1);
			resultvo.setStrCondition("");
			resultvo.setErrMsg("Canot find any dataprivileges list.");
			return resultvo;		
		}
		
		//step5:rolefunclistת����funcvolist
		MySQLFuncDAO mysqlfuncdao = new MySQLFuncDAO();
		mysqlfuncdao.setDatasource(datasource);
		
		List<FuncVO> funclist = mysqlfuncdao.getFuncList(rolefunclist);
		
		//step6:�������е�FuncCondtion
		MySQLFuncConditionDAO funcconditiondao = new MySQLFuncConditionDAO();
		funcconditiondao.setDatasource(datasource);
		
		Map<String,String> conditionmap = funcconditiondao.getAllConditionMap();
		
		//step7:����funclist���������ʽ��������FuncCondition�����ַ����滻
		//����rolefunclist ��  conditiomap����ϼ��㷵��ֵ
		//���յõ��ļ�����ʽ
		String strAllCondition="";
		for(FuncVO funcvo:funclist) {
			String strcond = funcvo.getFuncConditions();
			if(strcond==null || strcond.length()==0) {
				//ָ�����������ʽû������ֵ������������ѭ��
				continue;
			}
			
			//�����ʽ����ָ���������ַ��滻��
			//��strcond�е�ռλ���滻Ϊ�ض�Map����ı仯
			strcond = this.replaceMapvalue(strcond, conditionmap);
			
			if(strAllCondition.length()==0) {
				strAllCondition += "("+ strcond +")";
			}else {
				//�������������£�ʹ��OR�������������������
				strAllCondition += " OR ("+ strcond +")";
			}
		}
		
		//step8����Ϸ���ֵ����
		com.liu.commonqueryprivilege.UserDataPrivilegeResultVO dpresultvo = new com.liu.commonqueryprivilege.UserDataPrivilegeResultVO();
		dpresultvo.setIret(0);
		dpresultvo.setStrCondition(strAllCondition);
		dpresultvo.setErrMsg("����ɹ�.");
		return dpresultvo;
	}

	/**
	 * �������ݿ�����
	 */
	@Override
	public void setDataSource(DataSource ds) {
		this.datasource = ds;

	}
	
	private String replaceMapvalue(String strinput,Map<String,String> strMap) {
		String strout = strinput;
		Iterator<String> iter = strMap.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			String value = strMap.get(key);
			strout = strout.replaceAll("@"+key, value);
			strout = strout.replaceAll("\\$"+key, value);
			strout = strout.replaceAll("\\{"+key+"}", value);
		}
		return strout;
	}

	/**
	 * 利用qid来检索得到对应的查询funcid
	 */
	@Override
	public String getqueryfuncid(String qid) {
		MySQLFuncDAO mysqlfuncdao = new MySQLFuncDAO();
		mysqlfuncdao.setDatasource(datasource);
		
		FuncVO funcvo = mysqlfuncdao.getFuncVOByQueryid(qid);
		if(funcvo == null) {
			//return null.
			return null;
		}else {
			return funcvo.getFuncid();
		}

	}

}
