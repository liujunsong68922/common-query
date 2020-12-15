package com.liu.commonqueryprivilege.vo;

public class FuncVO {
	/**
	 * funcid,���ܱ���
	 */
	private String funcid;
	/**
	 * ���ʱ����Ȩ�޹������ʶ����Ȩ�ޱ��ʽ�����а���������ʽ
	 */
	private String funcConditions;
	
	/**
	 * ��ѯ����ʱ���������ݲ�ѯQid
	 */
	private String funcqid;
	
	/**
	 * ���ܷ��� 1������ 2.��ѯ���� 3.����Ȩ�޹���
	 */
	private String functype;
	
	public String getFuncid() {
		return funcid;
	}
	public void setFuncid(String funcid) {
		this.funcid = funcid;
	}
	public String getFuncConditions() {
		return funcConditions;
	}
	public void setFuncConditions(String funcConditions) {
		this.funcConditions = funcConditions;
	}
	public String getFuncqid() {
		return funcqid;
	}
	public void setFuncqid(String funcqid) {
		this.funcqid = funcqid;
	}

	
}
