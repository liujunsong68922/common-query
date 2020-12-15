package com.liu.commonquery.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;


@Service
public class DbShowService {
	@Resource(name = "dataSource")
	private DataSource datasource;

	private JdbcTemplate jdbcTemplate;

	public String getDataAsTable(String strsql) {
		jdbcTemplate = new JdbcTemplate(this.datasource);

		SqlRowSet rs = jdbcTemplate.queryForRowSet(strsql);

		SqlRowSetMetaData meta = rs.getMetaData();
		String colname[] = new String[meta.getColumnCount() + 1];

		String sret = "<TABLE class=\"pure-table pure-table-bordered\"> ";
		String sline = "";
		int col = 0;
		for (col = 1; col <= meta.getColumnCount(); col++) {
			colname[col] = meta.getColumnLabel(col);
			sline = sline + "<th>" + meta.getColumnLabel(col) + "</th>";
		}
		sret = sret + "<thead><tr>" + sline + "</tr></thead>";
		sret = sret + "<tbody>";
		sline ="";
		for (; rs.next(); ) {
			sline ="";
			for (col = 1; col <= meta.getColumnCount(); col++) {
				sline = sline + "<td>" + rs.getString(col) + "</td>";
			}
			sret = sret + "<tr>" + sline + "</tr>";
		}
		sret = sret +"</tbody>";
		sret = sret +"</TABLE>";
		return sret;
	}
}
