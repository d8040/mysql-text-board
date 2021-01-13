package com.sbs.example.mysqlTextBoard.dto;

import java.util.Map;

import lombok.Data;

@Data
public class Member {
	
	private String name;
	private String loginId;
	private String loginPw;
	private int id;
	private String regDate;
	private String updateDate;

	public String getType() {
		return isAdmin() ? "관리자" :"일번회원";
	}

	public boolean isAdmin() {
		return loginId.equals("aaa");
	}
	public Member(Map<String, Object> memberMap) {
		this.name = (String)memberMap.get("name");
		this.loginId = (String)memberMap.get("loginId");
		this.loginPw = (String)memberMap.get("loginPw");
		this.regDate = (String)memberMap.get("regDate");
		this.updateDate = (String)memberMap.get("updateDate");
		this.id = (int)memberMap.get("id");
	}


	@Override
	public String toString() {
		return "Member [name=" + name + ", loginId=" + loginId + ", loginPw=" + loginPw + ", id=" + id + ", regDate="
				+ regDate + ", updateDate=" + updateDate + "]";
	}
}
