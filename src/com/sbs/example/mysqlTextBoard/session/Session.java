package com.sbs.example.mysqlTextBoard.session;

public class Session {

	public int loginedMemberId;
	private String currentBoardCode;

	public Session() {
		currentBoardCode = "notice";
	}

	public void logout() {
		loginedMemberId = 0;
	}

	public boolean isLogined() {
		return loginedMemberId > 0;
	}

	public String getCurrentBoardCode() {
		return currentBoardCode;
	}

	public void setCurrentBoardCode(String code) {
		currentBoardCode = code;
	}

}
