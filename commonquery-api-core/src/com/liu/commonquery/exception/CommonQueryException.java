package com.liu.commonquery.exception;

public class CommonQueryException extends Exception{
	private String errinfo;

	public String getErrinfo() {
		return errinfo;
	}

	public void setErrinfo(String errinfo) {
		this.errinfo = errinfo;
	}
	
	/**
	 * constructor
	 * @param errmsg
	 */
	public CommonQueryException(String errmsg) {
		this.errinfo = errmsg;
	}
}
