package com.liu.commonquery.tokenuser.vo;

public class TokenUserVO {
	/**
	 * tokenid,from web content
	 */
	private String tokenid;
	/**
	 * userid,convert tokenid to userid;
	 */
	private String userid;
	public String getTokenid() {
		return tokenid;
	}
	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
}
