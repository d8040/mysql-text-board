package com.sbs.example.mysqlTextBoard.dto;

import java.util.Map;

public class Board {
	
	public Board(Map<String, Object> articleMap) {
		this.name = (String) articleMap.get("name");
		this.regDate = (String) articleMap.get("regDate");
		this.updateDate = (String) articleMap.get("updateDate");
		this.id = (int) articleMap.get("id");
	}
	public String name;
	public String regDate;
	public String updateDate;
	@Override
	public String toString() {
		return "Board [name=" + name + ", regDate=" + regDate + ", updateDate=" + updateDate + ", id=" + id + "]";
	}
	public int id;

}
