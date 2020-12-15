package com.liu.commonqueryprivilege;

import javax.sql.DataSource;

/**
 * �û�Ȩ�޵��ⲿ���ʽӿ��ࣨ�ǲ����ࣩ
 * @author liujunsong
 *
 */
public interface IUserDataPrivilege {
	/**
	 * set datasource
	 * @param ds
	 */
	public void setDataSource(DataSource ds);
	/**
	 * 查询一下指定用户，指定查询权限下面的数据权限表达式
	 * @param userid	用户ID
	 * @param queryfuncid	功能ID，这个功能ID是代表一个查询类的功能ID
	 * @return
	 */
	public UserDataPrivilegeResultVO getDataPrivilegeResultVO(String userid,String queryfuncid);

	/**
	 * 根据QueryId来逆向查找对应的funcId,每个QueryId只能用在一个funcId上（前提条件）
	 * @param qid
	 * @return
	 */
	public String getqueryfuncid(String qid);

}
