package com.liu.commonquery;

import com.liu.commonquery.impl.MySQLDataQueryImpl;

/**
 * 
 * @author liujunsong
 *
 */
public class DataQueryFactory {
	public IDataQuery getIDataQuery(String type) {
		if (type.equalsIgnoreCase("mysql")) {
			//mysql version DataQuery Service Impl.
			return new MySQLDataQueryImpl();
		}
		
		System.out.println("Unsupported type:"+type);
		return null;
	}
}
