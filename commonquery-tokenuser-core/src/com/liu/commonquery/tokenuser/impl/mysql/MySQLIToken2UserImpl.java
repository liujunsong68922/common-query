package com.liu.commonquery.tokenuser.impl.mysql;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.liu.commonquery.tokenuser.IToken2User;
import com.liu.commonquery.tokenuser.vo.TokenUserVO;

public class MySQLIToken2UserImpl implements IToken2User {
	private DataSource datasource;
	
	private String TOKENUSERTABLE="t_web_tokenuser";
	
	@Override
	public TokenUserVO getTokenUserVOByTokenId(String tokenid) {
		String strsql="select tokenid,userid from "+this.TOKENUSERTABLE;
		strsql += " Where tokenid='"+tokenid+"'";
		
		JdbcTemplate jt = new JdbcTemplate(this.datasource);
		
		List<Map<String,Object>> list1 = jt.queryForList(strsql);
		if(list1.size()==0) {
			return null;
		}else {
			//如果存在重复数据，则只取第一条（原则上tokenid不可重复）
			Map map1 = list1.get(0);
			TokenUserVO vo = new TokenUserVO();
			vo.setTokenid(tokenid);
			vo.setUserid((String)map1.get("userid"));
			return vo;
		}
	}

	/**
	 * 设置数据库的连接对象
	 */
	@Override
	public void setDataSource(DataSource ds) {
		this.datasource = ds;
	}

	/**
	 * 根据tokenid获取userid的信息
	 */
	@Override
	public String getUserIdByTokenId(String tokenid) {
		TokenUserVO vo = this.getTokenUserVOByTokenId(tokenid);
		if(vo==null) {
			return null;
		}else {
			return vo.getUserid();
		}
	}

}
