package com.sbs.example.mysqlTextBoard.dto;

public class Member {
	public Member(String loginId, String loginPw, String name, int id) {
		this.name = name;
		this.loginId = loginId;
		this.loginPw = loginPw;
		this.id = id;
	}
	public String name;
	public String loginId;
	public String loginPw;
	public int id;
}
