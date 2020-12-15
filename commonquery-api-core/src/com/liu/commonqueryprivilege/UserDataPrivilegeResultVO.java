package com.liu.commonqueryprivilege;

public class UserDataPrivilegeResultVO {
	/**
	 * 调用返回值，0 调用成功 -1 调用失败（发生错误）
	 */
	private int iret;
	/**
	 * 调用成功以后，返回的表达式，其中可能带有占位符需要后续处理
	 */
	private String strCondition;
	/**
	 *  调用失败时的，返回的错误信息给调用者
	 */
	private String errMsg;
	public int getIret() {
		return iret;
	}
	public void setIret(int iret) {
		this.iret = iret;
	}
	public String getStrCondition() {
		return strCondition;
	}
	public void setStrCondition(String strCondition) {
		this.strCondition = strCondition;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
}
