package com.liu.commonquery.tokenuser;

import javax.sql.DataSource;

import com.liu.commonquery.tokenuser.vo.TokenUserVO;

public interface IToken2User {
	/**
	 * ����tokenid����ȡTokenUserVO����
	 * @param tokenid
	 * @return
	 */
	public TokenUserVO getTokenUserVOByTokenId(String tokenid);
	
	/**
	 * ��������Դ�����Ӷ���
	 * @param ds
	 */
	public void setDataSource(DataSource ds);
	
	/**
	 * ��tokenidת����userid
	 * @param tokenid
	 * @return
	 */
	public String getUserIdByTokenId(String tokenid);
}
