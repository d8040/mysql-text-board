package com.sbs.example.mysqlTextBoard.dto;

import java.util.Map;

import lombok.Data;

@Data
public class Tag {
	private int id;
	private String updateDate;
	private String relTypeCode;
	private String regDate;
	private int relId;
	private String body;
	
	public Tag(Map<String, Object> map) {
		this.id = (int) map.get("id");
		this.relId = (int) map.get("relId");
		this.updateDate = (String) map.get("updateDate");
		this.relTypeCode = (String) map.get("relTypeCode");
		this.regDate = (String) map.get("regDate");
		this.body = (String) map.get("body");
	}
}