package com.sbs.example.mysqlTextBoard.session;

public class Session {

	public int loginedMemberId;
	public int selectBoardId = 1;

	public void logout() {
		loginedMemberId = 0;
	}

	public boolean isLogined() {
		return loginedMemberId > 0;
	}

}
