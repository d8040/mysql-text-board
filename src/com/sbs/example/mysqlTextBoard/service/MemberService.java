package com.sbs.example.mysqlTextBoard.service;

import com.sbs.example.mysqlTextBoard.Container;
import com.sbs.example.mysqlTextBoard.dao.MemberDao;

public class MemberService {

	private MemberDao memberDao;
	
	public MemberService() {
		memberDao = Container.memberDao;
	}

	public int join(String loginId, String loginPw, String name) {
		return memberDao.join(loginId, loginPw, name);
	}

}
