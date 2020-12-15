package com.liu.commonquery.tokenuser;

import javax.sql.DataSource;

import com.liu.commonquery.tokenuser.vo.TokenUserVO;

public interface IToken2User {
	/**
	 * 根据tokenid来获取TokenUserVO对象
	 * @param tokenid
	 * @return
	 */
	public TokenUserVO getTokenUserVOByTokenId(String tokenid);
	
	/**
	 * 设置数据源的连接对象
	 * @param ds
	 */
	public void setDataSource(DataSource ds);
	
	/**
	 * 把tokenid转换成userid
	 * @param tokenid
	 * @return
	 */
	public String getUserIdByTokenId(String tokenid);
}
