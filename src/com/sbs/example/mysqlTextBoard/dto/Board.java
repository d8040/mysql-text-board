package com.sbs.example.mysqlTextBoard.dto;

import java.util.Map;

import lombok.Data;

@Data
public class Board {

	private String name;
	private String regDate;
	private String updateDate;
	private int id;
	private String code;
	
	public Board(Map<String, Object> articleMap) {
		this.name = (String) articleMap.get("name");
		this.regDate = (String) articleMap.get("regDate");
		this.updateDate = (String) articleMap.get("updateDate");
		this.id = (int) articleMap.get("id");
		this.code = (String) articleMap.get("code");
	}
	@Override
	public String toString() {
		return "Board [name=" + name + ", regDate=" + regDate + ", updateDate=" + updateDate + ", id=" + id + ", code="
				+ code + "]";
	}
}
