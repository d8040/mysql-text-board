package com.sbs.example.mysqlTextBoard.session;

public class Session {

	public int loginedMemberId;

	public void logout() {
		loginedMemberId = 0;
	}

}
